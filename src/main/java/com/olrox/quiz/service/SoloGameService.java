package com.olrox.quiz.service;

import com.olrox.quiz.entity.SoloGame;
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
    @Autowired
    private SoloGamePrototypeService prototypeService;

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

        // FIXME maybe need to add game to prototype set
        var prototype = prototypeService.createPrototype(user, questions, timeForQuestionInSeconds);

        SoloGame soloGame = new SoloGame();
        soloGame.setParticipant(user);
        soloGame.setStatus(SoloGame.Status.IN_PROGRESS);
        soloGame.setCreationTime(LocalDateTime.now());
        soloGame.setPrototype(prototype);

        soloGameRepository.save(soloGame);

        SoloGameProcess newProcess = new SoloGameProcess(soloGame);
        activeGames.put(soloGame.getId(), newProcess);

        return soloGame.getId();
    }

    public List<SoloGame> deleteGamesInProgress() {
        return soloGameRepository.deleteAllByStatusEquals(SoloGame.Status.IN_PROGRESS);
    }

    public List<SoloGame> loadGamesInProgressFromDb() {
        return soloGameRepository.findAllByStatusEquals(SoloGame.Status.IN_PROGRESS);
    }

    public SoloGameProcess getGameProcessById(Long id) {
        return activeGames.get(id);
    }

    public Optional<SoloGame> getGame(Long id) {
        return soloGameRepository.findById(id);
    }

    public SoloGame finishGame(SoloGameProcess process) {
        var gameToFinish = process.getSoloGame();
        activeGames.remove(gameToFinish.getId());

        gameToFinish.setStatus(SoloGame.Status.FINISHED);
        gameToFinish.setCorrectAnswersCount(
                resultService.countCorrectAnswers(process.getResults()));

        return soloGameRepository.save(gameToFinish);

        // return resultService.saveTotalResult(gameToFinish, process.getParticipant(), process.getResults());
    }

    public Optional<SoloGame> findFinishedGame(Long id) {
        return soloGameRepository.findFirstByIdEqualsAndStatusEquals(id, SoloGame.Status.FINISHED);
    }
}
