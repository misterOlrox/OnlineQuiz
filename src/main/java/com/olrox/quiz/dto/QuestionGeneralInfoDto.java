package com.olrox.quiz.dto;

import com.olrox.quiz.entity.QuizQuestion;
import lombok.Data;

@Data
public class QuestionGeneralInfoDto {
    private long id;
    private String question;

    public static QuestionGeneralInfoDto from(QuizQuestion quizQuestion) {
        var dto = new QuestionGeneralInfoDto();
        dto.setId(quizQuestion.getId());
        dto.setQuestion(quizQuestion.getQuestion());

        return dto;
    }
}
