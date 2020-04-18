package com.olrox.quiz.service;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.UserAnswer;
import com.olrox.quiz.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public List<UserAnswer> findUserAnswers(SoloGame soloGame) {
        return userAnswerRepository.findAllByGameEquals(soloGame);
    }
}
