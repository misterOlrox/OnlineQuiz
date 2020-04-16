package com.olrox.quiz.dto;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.process.SoloGameProcess;

public class GameProcessInfo implements InfoDto {
    private final String type = "game.process.info";
    private Long id;
    private String creator;
    private Long timeForQuestionInMillis;
    private Integer numberOfQuestions;
    private SoloGame.Status status;

    public static GameProcessInfo from(SoloGameProcess soloGameProcess) {
        GameProcessInfo gameProcessInfo = new GameProcessInfo();
        gameProcessInfo.id = soloGameProcess.getSoloGame().getId();
        gameProcessInfo.creator = soloGameProcess.getSoloGame().getCreator().getUsername();
        gameProcessInfo.status = soloGameProcess.getSoloGame().getStatus();
        gameProcessInfo.timeForQuestionInMillis = (long) soloGameProcess.getSoloGame().getTimeForQuestionInSeconds() * 1000;
        gameProcessInfo.numberOfQuestions = soloGameProcess.getSoloGame().getNumberOfQuestions();

        return gameProcessInfo;
    }

    public Long getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }

    public Long getTimeForQuestionInMillis() {
        return timeForQuestionInMillis;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public SoloGame.Status getStatus() {
        return status;
    }

    @Override
    public String getType() {
        return type;
    }
}
