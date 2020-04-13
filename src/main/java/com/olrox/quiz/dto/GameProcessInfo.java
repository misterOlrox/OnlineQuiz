package com.olrox.quiz.dto;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.process.SoloGameProcess;

public class GameProcessInfo {
    private Long id;
    private String creator;
    private Integer timeForQuestionInSeconds;
    private Integer numberOfQuestions;
    private SoloGame.Status status;

    public static GameProcessInfo from(SoloGameProcess soloGameProcess) {
        GameProcessInfo gameProcessInfo = new GameProcessInfo();
        gameProcessInfo.id = soloGameProcess.getSoloGame().getId();
        gameProcessInfo.creator = soloGameProcess.getSoloGame().getCreator().getUsername();
        gameProcessInfo.status = soloGameProcess.getSoloGame().getStatus();
        gameProcessInfo.timeForQuestionInSeconds = soloGameProcess.getSoloGame().getTimeForQuestionInSeconds();
        gameProcessInfo.numberOfQuestions = soloGameProcess.getSoloGame().getNumberOfQuestions();

        return gameProcessInfo;
    }

    public Long getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }

    public Integer getTimeForQuestionInSeconds() {
        return timeForQuestionInSeconds;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public SoloGame.Status getStatus() {
        return status;
    }
}
