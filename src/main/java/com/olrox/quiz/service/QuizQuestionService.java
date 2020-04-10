package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.QuizQuestionRepository;
import com.olrox.quiz.repository.QuizQuestionThemeRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class QuizQuestionService {

    @Autowired private QuizQuestionRepository quizQuestionRepository;

    @Autowired private QuizQuestionThemeRepository quizQuestionThemeRepository;

    public QuizQuestion addQuestion(
            User author,
            String question,
            String correctAnswer,
            String wrongAnswersJson,
            boolean approved,
            QuizQuestionTheme theme
    ) {

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
            boolean approved
    ) {

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
            List<String> wrongAnswers,
            List<Long> selectedThemes
    ) {

        String wrongAnswersJson = (new JSONArray(wrongAnswers)).toString();

        boolean approved = author.isAdmin();
        QuizQuestion quizQuestion = getQuizQuestionWithFields(
                author,
                question,
                correctAnswer,
                wrongAnswersJson,
                approved
        );

        if (!selectedThemes.isEmpty()) {
            var themes = quizQuestionThemeRepository.findAllById(selectedThemes);
            quizQuestion.setThemes(Set.copyOf(themes));
        }

        return quizQuestionRepository.save(quizQuestion);
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

    public List<Long> getQuestionIdsByThemes(Collection<QuizQuestionTheme> themes) {


        return null;
    }
}
