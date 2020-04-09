package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.QuizQuestionRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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

        QuizQuestion quizQuestion = getQuizQuestionWithFields(
                author,
                question,
                correctAnswer,
                wrongAnswersJson,
                approved
        );
        quizQuestion.addTheme(theme);

        return quizQuestionRepository.save(quizQuestion);
    }

    public QuizQuestion addQuestion(
            User author,
            String question,
            String correctAnswer,
            String wrongAnswersJson,
            boolean approved) {

        QuizQuestion quizQuestion = getQuizQuestionWithFields(
                author,
                question,
                correctAnswer,
                wrongAnswersJson,
                approved
        );

        return quizQuestionRepository.save(quizQuestion);
    }

    public QuizQuestion addQuestion(
            User author,
            String question,
            String correctAnswer,
            List<String> wrongAnswers) {

        String wrongAnswersJson = (new JSONArray(wrongAnswers)).toString();

        if (author.isAdmin()) {
            return addQuestion(author, question, correctAnswer, wrongAnswersJson,true);
        } else {
            return addQuestion(author, question, correctAnswer, wrongAnswersJson,false);
        }
    }

    private QuizQuestion getQuizQuestionWithFields(
            User author,
            String question,
            String correctAnswer,
            String wrongAnswersJson,
            boolean approved) {

        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setAuthor(author);
        quizQuestion.setQuestion(question);
        quizQuestion.setCorrectAnswer(correctAnswer);
        quizQuestion.setWrongAnswersJson(wrongAnswersJson);
        quizQuestion.setApproved(approved);

        return quizQuestion;
    }

    // TODO
    public List<Long> getQuestionIdsByThemes(Collection<QuizQuestionTheme> themes) {
        return null;
    }
}
