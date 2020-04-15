package com.olrox.quiz.service;

import com.olrox.quiz.dto.GameProcessInfo;
import com.olrox.quiz.dto.TimeoutInfoDto;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.SoloGameResult;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.process.SoloGameProcess;
import com.olrox.quiz.repository.SoloGameRepository;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SoloGameService {

    private final static Logger LOG = LoggerFactory.getLogger(SoloGameService.class);

    @Autowired
    private QuizQuestionService quizQuestionService;
    @Autowired
    private SoloGameRepository soloGameRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SoloGameResultService resultService;

    private Map<Long, SoloGameProcess> activeGames = new ConcurrentHashMap<>();
    private Map<Long, Thread> tasks = new ConcurrentHashMap<>();
    private Clock clock = Clock.systemDefaultZone();

    public Long generateSoloGame(
            User user,
            Integer timeForQuestionInSeconds,
            Integer numberOfQuestions,
            List<Long> themesIds
    ) {

        var questions = quizQuestionService.findRandomQuestions(numberOfQuestions, themesIds);
        var foundSize = questions.size();
        if (foundSize != numberOfQuestions) {
            LOG.warn("Required questions: {}. Found: {}", numberOfQuestions, foundSize);
        }
        if (foundSize == 0) {
            throw new RuntimeException(
                    "Haven't any questions for themes " + new JSONArray(themesIds));
        }

        SoloGame soloGame = new SoloGame();
        soloGame.setCreator(user);
        soloGame.setTimeForQuestionInSeconds(timeForQuestionInSeconds);
        soloGame.setStatus(SoloGame.Status.IN_PROGRESS);
        soloGame.setNumberOfQuestions(foundSize);
        soloGame.setQuestionList(questions);
        soloGame.setCreationTime(LocalDateTime.now());

        soloGameRepository.save(soloGame);

        SoloGameProcess newProcess = new SoloGameProcess(soloGame, user);
        activeGames.put(soloGame.getId(), newProcess);
        addTimeoutNotificationTask(newProcess);

        return soloGame.getId();
    }

    public List<SoloGame> deleteGamesInProgress() {
        return soloGameRepository.deleteAllByStatusEquals(SoloGame.Status.IN_PROGRESS);
    }

    public List<SoloGame> loadGamesInProgressFromDb() {
        return soloGameRepository.findAllByStatusEquals(SoloGame.Status.IN_PROGRESS);
    }

    public List<SoloGame> getGamesCreatedBy(User user) {
        return soloGameRepository.findAllByCreator(user);
    }

    public SoloGameProcess getGameProcessById(Long id) {
        return activeGames.get(id);
    }

    public SoloGameResult finishGame(SoloGameProcess process) {
        var gameToFinish = process.getSoloGame();
        activeGames.remove(gameToFinish.getId());

        Thread task = tasks.remove(gameToFinish.getId());
        task.interrupt();

        gameToFinish.setStatus(SoloGame.Status.FINISHED);
        soloGameRepository.save(gameToFinish);

        return resultService.saveTotalResult(gameToFinish, process.getParticipant(), process.getResults());
    }

    private void addTimeoutNotificationTask(SoloGameProcess process) {
        final long id = process.getSoloGame().getId();
        final long timeForQuestion = process.getTimeForQuestionInMillis();
        final String notificationDest = "/topic/solo/game/info/" + id;

        messagingTemplate.convertAndSend(notificationDest, GameProcessInfo.from(process));

        Thread task = new Thread(()-> {
            int i = 0;
            while (!process.isFinished()) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    LOG.warn("Task with id [{}] was interrupted", id);
                }
                TimeoutInfoDto info;
                var pair = process.getCurrentIndAndNextTimeout();
                int questionInd = pair.getFirst();
                long nextTimeout = pair.getSecond();
                long timeLeft = nextTimeout - clock.millis();
                if (timeLeft <= 0) {
                    info = new TimeoutInfoDto(true, 0, questionInd);
                } else {
                    info = new TimeoutInfoDto(false, timeLeft, questionInd);
                }
                messagingTemplate.convertAndSend(notificationDest, info);
            }

            LOG.info("Task thread with id [{}] was successfully done", id);
        });
        tasks.put(id, task);
        task.start();
    }
}
