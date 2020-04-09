package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.repository.QuizQuestionThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizQuestionThemeService {

    @Autowired
    private QuizQuestionThemeRepository quizQuestionThemeRepository;

    public QuizQuestionTheme add(QuizQuestionTheme theme) {
        return quizQuestionThemeRepository.save(theme);
    }

    public List<QuizQuestionTheme> getAllThemes() {
        return quizQuestionThemeRepository.findAll();
    }

}
