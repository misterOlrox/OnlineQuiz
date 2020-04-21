package com.olrox.quiz.service;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.projection.UserStatProjection;
import com.olrox.quiz.projection.UserStatType;
import com.olrox.quiz.repository.GamePrototypeRepository;
import com.olrox.quiz.repository.QuizQuestionRepository;
import com.olrox.quiz.repository.SoloGameRepository;
import com.olrox.quiz.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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

    private Set<GamePrototype.Type> quizCreatedAllowedTypes = Set.of(
            GamePrototype.Type.SOLO_SHARED_PRIVATE,
            GamePrototype.Type.SOLO_SHARED_PUBLIC
    );

    private Map<UserStatType, Function<Integer, Page<UserStatProjection>>> usersTopFunctionMap;

    @PostConstruct
    private void postConstruct() {
        usersTopFunctionMap = Map.of(
                UserStatType.QUIZ_PLAYED, this::getTopNUsersByQuizPlayed,
                UserStatType.QUIZ_CREATED, this::getTopNUsersByQuizCreated,
                UserStatType.QUESTIONS_ADDED, this::getTopNUsersByQuestionsAdded,
                UserStatType.CORRECT_ANSWERS, this::getTopNUsersByCorrectAnswers
        );
    }

    public Long getQuizPlayedCount(User user) {
        return soloGameRepository.countAllByParticipantEqualsAndStatusEquals(
                user, SoloGame.Status.FINISHED);
    }

    public Long getQuizCreatedCount(User creator) {
        return gamePrototypeRepository.countAllByCreatorEqualsAndTypeIn(
                creator, quizCreatedAllowedTypes);
    }

    public Long getCorrectAnswersCount(User user) {
        return userAnswerRepository.countCorrectAnswersByUser(user);
    }

    public Long getQuestionsAddedCount(User user) {
        return quizQuestionRepository.countAllByAuthorEquals(user);
    }

    public Page<UserStatProjection> getTopNUsersByByStat(int n, UserStatType statType) {
        var statFunctionRef = usersTopFunctionMap.get(statType);
        if (statFunctionRef == null) {
            return null;
        } else {
            return statFunctionRef.apply(n);
        }
    }

    public Page<UserStatProjection> getTopNUsersByQuizPlayed(int n) {
        return soloGameRepository.findTopByQuizPlayed(PageRequest.of(0, n));
    }

    public Page<UserStatProjection> getTopNUsersByQuizCreated(int n) {
        return gamePrototypeRepository.findTopByQuizCreated(
                PageRequest.of(0, n), quizCreatedAllowedTypes);
    }

    public Page<UserStatProjection> getTopNUsersByQuestionsAdded(int n) {
        return quizQuestionRepository.findTopByQuestionsAdded(PageRequest.of(0, n));
    }

    public Page<UserStatProjection> getTopNUsersByCorrectAnswers(int n) {
        return userAnswerRepository.findTopByCorrectAnswer(PageRequest.of(0, n));
    }
}
