package com.olrox.quiz.dto;

import lombok.Data;

@Data
public class NextQuestionAndPrevResultDto {
    private AnswerResultDto prevResult;
    private QuestionDto nextQuestion;
}
