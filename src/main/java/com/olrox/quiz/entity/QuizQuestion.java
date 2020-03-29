package com.olrox.quiz.entity;

import org.json.JSONArray;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "quiz_question_table")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String correctAnswer;

    private String wrongAnswersJson;

    @ManyToOne
    private User author;

    private boolean approved;

    @ManyToMany
    private Set<User> approvals;

    @ManyToOne
    public QuizQuestionTheme theme;

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

    public String getWrongAnswersJson() {
        return wrongAnswersJson;
    }

    public void setWrongAnswersJson(String wrongAnswersJson) {
        this.wrongAnswersJson = wrongAnswersJson;
    }

    public void setWrongAnswersJson(Collection<String> wrongAnswers) {
        this.wrongAnswersJson = new JSONArray(wrongAnswers).toString();
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
}
