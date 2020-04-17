package com.olrox.quiz.dto;

import com.olrox.quiz.entity.UserAnswer;
import lombok.Data;

@Data
public class AnswerResultDto {
    private String yourAnswer;
    private String correctAnswer;
    private UserAnswer.Status status;
    private String question;

    public static AnswerResultDto from(UserAnswer userAnswer) {
        AnswerResultDto dto = new AnswerResultDto();
        dto.status = userAnswer.getStatus();
        dto.correctAnswer = userAnswer.getQuizQuestion().getCorrectAnswer();
        dto.yourAnswer = userAnswer.getAnswer();
        dto.question = userAnswer.getQuizQuestion().getQuestion();

        return dto;
    }

}
