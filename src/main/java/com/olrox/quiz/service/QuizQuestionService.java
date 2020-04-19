package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.WrongAnswer;
import com.olrox.quiz.repository.QuizQuestionRepository;
import com.olrox.quiz.repository.QuizQuestionThemeRepository;
import com.olrox.quiz.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class QuizQuestionService {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizQuestionThemeRepository quizQuestionThemeRepository;

    public QuizQuestion addQuestion(
            User author,
            String question,
            String correctAnswer,
            List<WrongAnswer> wrongAnswers,
            boolean approved,
            QuizQuestionTheme theme
    ) {

        QuizQuestion quizQuestion = getQuizQuestionWithFields(
                author,
                question,
                correctAnswer,
                wrongAnswers,
                approved
        );
        quizQuestion.addTheme(theme);

        return quizQuestionRepository.save(quizQuestion);
    }

    public QuizQuestion addQuestion(
            User author,
            String question,
            String correctAnswer,
            List<WrongAnswer> wrongAnswers,
            boolean approved
    ) {

        QuizQuestion quizQuestion = getQuizQuestionWithFields(
                author,
                question,
                correctAnswer,
                wrongAnswers,
                approved
        );

        return quizQuestionRepository.save(quizQuestion);
    }

    public QuizQuestion addQuestion(
            User author,
            String question,
            String correctAnswer,
            List<WrongAnswer> wrongAnswers,
            List<Long> selectedThemes
    ) {

        boolean approved = author.isAdmin();
        QuizQuestion quizQuestion = getQuizQuestionWithFields(
                author,
                question,
                correctAnswer,
                wrongAnswers,
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
            List<WrongAnswer> wrongAnswers,
            boolean approved) {

        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setAuthor(author);
        quizQuestion.setQuestion(question);
        quizQuestion.setCorrectAnswer(correctAnswer);
        quizQuestion.setWrongAnswers(wrongAnswers);
        quizQuestion.setApproved(approved);

        wrongAnswers.forEach(x -> x.setQuizQuestion(quizQuestion));

        return quizQuestion;
    }

    public List<QuizQuestion> findRandomQuestions(int number, List<Long> themesIds) {
        var questionsIds = quizQuestionRepository.getQuestionIdsByThemes(themesIds);
        var randomQuestionIds = RandomUtil.pickNRandomElements(questionsIds, number);

        return quizQuestionRepository.findAllById(randomQuestionIds);
    }

    public List<QuizQuestion> findAllByThemeId(Long themeId) {
        return quizQuestionRepository.findAllByThemeId(themeId);
    }

    public Page<QuizQuestion> findAllByThemeId(Long themeId, Pageable pageable) {
        return quizQuestionRepository.findAllByThemeId(themeId, pageable);
    }
}
