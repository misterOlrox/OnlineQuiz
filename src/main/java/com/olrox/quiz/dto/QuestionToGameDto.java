package com.olrox.quiz.dto;

import com.olrox.quiz.entity.QuizQuestion;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class QuestionToGameDto {
    private Integer number;
    private String question;
    private Long timeLeft;
    private List<String> answers = new ArrayList<>();

    public QuestionToGameDto(QuizQuestion quizQuestion) {
        this.question = quizQuestion.getQuestion();

        answers.add(quizQuestion.getCorrectAnswer());
        quizQuestion.getWrongAnswers().forEach(x -> answers.add(x.getValue()));
        Collections.shuffle(answers);
    }
}
