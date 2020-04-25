package com.olrox.quiz.dto;

import com.olrox.quiz.entity.GamePrototype;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GamePrototypeDto {
    private long id;
    private String name;
    private int numberOfQuestions;
    private long timeForQuestionsInSeconds;

    public static GamePrototypeDto from(GamePrototype gamePrototype) {
        return new GamePrototypeDto(
                gamePrototype.getId(),
                gamePrototype.getName(),
                gamePrototype.getNumberOfQuestions(),
                gamePrototype.getTimeForQuestionInSeconds()
        );
    }
}
