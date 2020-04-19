package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.SoloGamePrototype;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.SoloGamePrototypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePrototypeService {

    @Autowired
    private SoloGamePrototypeRepository prototypeRepository;

    public SoloGamePrototype createPrototype(
            User creator,
            List<QuizQuestion> questionList,
            Integer timeForQuestionInSeconds
    ) {
        SoloGamePrototype prototype = new SoloGamePrototype();
        prototype.setCreator(creator);
        prototype.setQuestionList(questionList);
        prototype.setNumberOfQuestions(questionList.size());
        prototype.setTimeForQuestionInSeconds(timeForQuestionInSeconds);

        return prototypeRepository.save(prototype);
    }
}
