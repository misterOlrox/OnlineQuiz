package com.olrox.quiz.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "solo_game_answer_table")
public class UserAnswer {
    public enum Status {
        CORRECT,
        WRONG,
        TIMEOUT,
        UNKNOWN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SoloGame game;

    @ManyToOne
    private QuizQuestion quizQuestion;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public QuizQuestion getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(QuizQuestion quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SoloGame getGame() {
        return game;
    }

    public void setGame(SoloGame game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAnswer that = (UserAnswer) o;
        return Objects.equals(game, that.game) &&
                Objects.equals(quizQuestion, that.quizQuestion) &&
                Objects.equals(answer, that.answer) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, quizQuestion, answer, status);
    }
}
