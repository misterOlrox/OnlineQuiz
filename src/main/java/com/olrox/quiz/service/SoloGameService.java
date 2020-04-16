package com.olrox.quiz.service;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public Optional<SoloGame> getGame(Long id) {
        return soloGameRepository.findById(id);
    }

    public SoloGameResult finishGame(SoloGameProcess process) {
        var gameToFinish = process.getSoloGame();
        activeGames.remove(gameToFinish.getId());

        gameToFinish.setStatus(SoloGame.Status.FINISHED);
        soloGameRepository.save(gameToFinish);

        return resultService.saveTotalResult(gameToFinish, process.getParticipant(), process.getResults());
    }
}
