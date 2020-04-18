package com.olrox.quiz.service;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.UserAnswer;
import com.olrox.quiz.process.SoloGameProcess;
import com.olrox.quiz.repository.SoloGameRepository;
import com.olrox.quiz.repository.UserAnswerRepository;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SoloGameService {

    private final static Logger LOG = LoggerFactory.getLogger(SoloGameService.class);

    @Autowired
    private SoloGameRepository soloGameRepository;
    @Autowired
    private UserAnswerRepository userAnswerRepository;
    @Autowired
    private SoloGamePrototypeService prototypeService;
    @Autowired
    private QuizQuestionService quizQuestionService;

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

    public void finishGame(SoloGameProcess process) {
        var gameToFinish = process.getSoloGame();

        if (gameToFinish.getStatus() == SoloGame.Status.FINISHED) {
            LOG.warn("Game [{}] already finished", gameToFinish);
            return;
        }

        activeGames.remove(gameToFinish.getId());

        var userAnswers = process.getUserAnswers();
        gameToFinish.setStatus(SoloGame.Status.FINISHED);
        gameToFinish.setFinishTime(LocalDateTime.now());
        gameToFinish.setCorrectAnswersCount(countCorrectAnswers(userAnswers));

        soloGameRepository.save(gameToFinish);
        userAnswerRepository.saveAll(userAnswers);
        LOG.info("Game [{}] finished, answers are saved", gameToFinish);
    }

    private int countCorrectAnswers(List<UserAnswer> userAnswers) {
        return (int) userAnswers.stream()
                .filter(x -> x.getStatus() == UserAnswer.Status.CORRECT)
                .count();
    }

    public Optional<SoloGame> findFinishedGame(Long id) {
        return soloGameRepository.findFirstByIdEqualsAndStatusEquals(id, SoloGame.Status.FINISHED);
    }

    @Transactional
    public List<SoloGame> findGamesByParticipant(User participant) {
        return soloGameRepository.findAllByParticipant(participant);
    }
}
