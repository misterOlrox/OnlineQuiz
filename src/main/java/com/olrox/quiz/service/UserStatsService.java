package com.olrox.quiz.service;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.GamePrototypeRepository;
import com.olrox.quiz.repository.QuizQuestionRepository;
import com.olrox.quiz.repository.SoloGameRepository;
import com.olrox.quiz.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserStatsService {

    @Autowired
    private SoloGameRepository soloGameRepository;
    @Autowired
    private GamePrototypeRepository gamePrototypeRepository;
    @Autowired
    private UserAnswerRepository userAnswerRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    public Long getQuizPlayedCount(User user) {
        return soloGameRepository.countAllByParticipantEqualsAndStatusEquals(
                user, SoloGame.Status.FINISHED);
    }

    public Long getQuizCreatedCount(User creator) {
        return gamePrototypeRepository.countAllByCreatorEqualsAndTypeIn(
                creator,
                Set.of(GamePrototype.Type.SOLO_SHARED_PRIVATE,
                        GamePrototype.Type.SOLO_SHARED_PUBLIC
                )
        );
    }

    public Long getCorrectAnswersCount(User user) {
        return userAnswerRepository.countCorrectAnswersByUser(user);
    }

    public Long getQuestionsAddedCount(User user) {
        return quizQuestionRepository.countAllByAuthorEquals(user);
    }
}
