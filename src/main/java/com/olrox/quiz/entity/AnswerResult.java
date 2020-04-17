package com.olrox.quiz.entity;

import java.util.Objects;

//@Entity
//@Table(name = "solo_game_answers_table")
public class AnswerResult {
    public enum Status {
        CORRECT,
        WRONG,
        TIMEOUT,
        UNKNOWN
    }

    private String answer;
    private Status status;
    private QuizQuestion quizQuestion;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerResult that = (AnswerResult) o;
        return Objects.equals(answer, that.answer) &&
                status == that.status &&
                Objects.equals(quizQuestion, that.quizQuestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer, status, quizQuestion);
    }

    @Override
    public String toString() {
        return "AnswerResult{" +
                "answer='" + answer + '\'' +
                ", status=" + status +
                ", quizQuestion=" + quizQuestion +
                '}';
    }
}
