package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.SoloGamePrototypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePrototypeService {

    @Autowired
    private SoloGamePrototypeRepository prototypeRepository;

    public GamePrototype createPrototype(
            User creator,
            GamePrototype.Type type,
            List<QuizQuestion> questionList,
            Integer timeForQuestionInSeconds
    ) {
        GamePrototype prototype = new GamePrototype();
        prototype.setCreator(creator);
        prototype.setType(type);
        prototype.setQuestionList(questionList);
        prototype.setNumberOfQuestions(questionList.size());
        prototype.setTimeForQuestionInSeconds(timeForQuestionInSeconds);

        return prototypeRepository.save(prototype);
    }
}
