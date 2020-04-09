package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SoloGameService {

    // TODO
    public String generateGameRoom(
            User user,
            Integer timeForQuestionInSeconds,
            Integer numberOfQuestions,
            Collection<QuizQuestionTheme> themes) {

        SoloGame soloGame = new SoloGame();
        soloGame.setCreator(user);
        soloGame.setTimeForQuestionInSeconds(timeForQuestionInSeconds);
        soloGame.setNumberOfQuestions(numberOfQuestions);
        soloGame.setStatus(SoloGame.Status.IN_PROGRESS);


        return "0";
    }
}
