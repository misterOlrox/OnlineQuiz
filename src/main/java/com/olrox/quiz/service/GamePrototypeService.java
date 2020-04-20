package com.olrox.quiz.service;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.GamePrototypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GamePrototypeService {

    @Autowired
    private GamePrototypeRepository prototypeRepository;

    public GamePrototype createPrototype(
            User creator,
            GamePrototype.Type type,
            String prototypeName,
            List<QuizQuestion> questionList,
            Integer timeForQuestionInSeconds
    ) {
        GamePrototype prototype = new GamePrototype();
        prototype.setCreator(creator);
        prototype.setType(type);
        prototype.setName(prototypeName);
        prototype.setQuestionList(questionList);
        prototype.setNumberOfQuestions(questionList.size());
        prototype.setTimeForQuestionInSeconds(timeForQuestionInSeconds);

        return prototypeRepository.save(prototype);
    }

    public List<GamePrototype> findSharedPrototypesByCreator(User creator) {
        return prototypeRepository.findAllSharedByCreator(creator.getId());
    }

    public Set<GamePrototype> findAllPublicPrototypes() {
        return prototypeRepository.findAllPublic();
    }
}
