package com.olrox.quiz.service;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.SoloGameRepository;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoloGameService {

    private final static Logger LOG = LoggerFactory.getLogger(SoloGameService.class);

    @Autowired
    private QuizQuestionService quizQuestionService;
    @Autowired
    private SoloGameRepository soloGameRepository;

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

        soloGameRepository.save(soloGame);

        return soloGame.getId();
    }
}
