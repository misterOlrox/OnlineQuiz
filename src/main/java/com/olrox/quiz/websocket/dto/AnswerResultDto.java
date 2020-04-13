package com.olrox.quiz.websocket.dto;

import com.olrox.quiz.entity.AnswerResult;
import lombok.Data;

@Data
public class AnswerResultDto {
    private String yourAnswer;
    private String correctAnswer;
    private AnswerResult.Status status;
    private String question;

    public static AnswerResultDto from(AnswerResult answerResult) {
        AnswerResultDto dto = new AnswerResultDto();
        dto.status = answerResult.getStatus();
        dto.correctAnswer = answerResult.getQuizQuestion().getCorrectAnswer();
        dto.yourAnswer = answerResult.getAnswer();
        dto.question = answerResult.getQuizQuestion().getQuestion();

        return dto;
    }

}
