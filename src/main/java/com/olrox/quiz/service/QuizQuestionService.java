package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizQuestionService {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    public QuizQuestion addQuestion(
            User author,
            String question,
            String correctAnswer,
            String wrongAnswersJson,
            boolean approved,
            QuizQuestionTheme theme) {

        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setAuthor(author);
        quizQuestion.setQuestion(question);
        quizQuestion.setCorrectAnswer(correctAnswer);
        quizQuestion.setWrongAnswersJson(wrongAnswersJson);
        quizQuestion.setApproved(approved);
        quizQuestion.addTheme(theme);

        return quizQuestionRepository.save(quizQuestion);
    }
}
