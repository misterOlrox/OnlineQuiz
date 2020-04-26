package com.olrox.quiz.service;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.WrongAnswer;
import com.olrox.quiz.repository.QuizQuestionRepository;
import com.olrox.quiz.repository.QuizQuestionThemeRepository;
import com.olrox.quiz.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class QuizQuestionService {

    public static final Logger LOG = LoggerFactory.getLogger(QuizQuestionService.class);

    public static final int REQUIRED_APPROVALS = 3;

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
        QuizQuestion quizQuestion = constructQuizQuestion(
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
        QuizQuestion quizQuestion = constructQuizQuestion(
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
        QuizQuestion quizQuestion = constructQuizQuestion(
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

    private QuizQuestion constructQuizQuestion(
            User author,
            String question,
            String correctAnswer,
            List<WrongAnswer> wrongAnswers,
            boolean approved
    ) {
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setAuthor(author);
        quizQuestion.setQuestion(question);
        quizQuestion.setCorrectAnswer(correctAnswer);
        quizQuestion.setWrongAnswers(wrongAnswers);
        quizQuestion.setApproved(approved);

        wrongAnswers.forEach(x -> x.setQuizQuestion(quizQuestion));

        return quizQuestion;
    }

    public List<QuizQuestion> findRandomAndApprovedQuestions(int number, List<Long> themesIds) {
        var questionsIds = quizQuestionRepository.getApprovedQuestionIdsByThemes(themesIds);
        var randomQuestionIds = RandomUtil.pickNRandomElements(questionsIds, number);

        return quizQuestionRepository.findAllById(randomQuestionIds);
    }

    public List<QuizQuestion> findUnapprovedQuestionsFor(User moderator) {
        return quizQuestionRepository.findUnapprovedQuestionsFor(moderator);
    }

    public void approveQuestion(Long questionId, User moderator) {
        QuizQuestion question = quizQuestionRepository.findById(questionId).get();
        question.getApprovals().add(moderator);
        if (question.getApprovals().size() >= REQUIRED_APPROVALS) {
            LOG.info("Question with id [{}] has been approved", question.getId());
            question.setApproved(true);
        }

        quizQuestionRepository.save(question);
    }

    public List<QuizQuestion> findAllById(List<Long> quizQuestionIds) {
        return quizQuestionRepository.findAllById(quizQuestionIds);
    }

    public List<QuizQuestion> findAllByThemeId(Long themeId) {
        return quizQuestionRepository.findAllByThemeId(themeId);
    }

    public Page<QuizQuestion> findAllByThemeId(Long themeId, Pageable pageable) {
        return quizQuestionRepository.findAllByThemeId(themeId, pageable);
    }

    public Page<QuizQuestion> findPrivateQuestions(User user, Pageable pageable) {
        return quizQuestionRepository.findPrivateQuestions(user.getId(), pageable);
    }
}
