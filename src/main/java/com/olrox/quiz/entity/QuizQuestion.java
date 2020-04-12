package com.olrox.quiz.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "quiz_question_table")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String correctAnswer;

    @OneToMany(mappedBy = "quizQuestion", cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WrongAnswer> wrongAnswers;

    @ManyToOne
    private User author;

    private boolean approved;

    @ManyToMany
    private Set<User> approvals;

    @ManyToMany
    @JoinTable(name = "quiz_question_join_themes",
            joinColumns = @JoinColumn(name = "quiz_question_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id"))
    public Set<QuizQuestionTheme> themes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<WrongAnswer> getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(List<WrongAnswer> wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Set<User> getApprovals() {
        return approvals;
    }

    public void setApprovals(Set<User> approvals) {
        this.approvals = approvals;
    }

    public Set<QuizQuestionTheme> getThemes() {
        return themes;
    }

    public void addTheme(QuizQuestionTheme theme) {
        getThemes().add(theme);
        theme.getQuestions().add(this);
    }

    public void setThemes(Set<QuizQuestionTheme> theme) {
        this.themes = theme;
    }
}
