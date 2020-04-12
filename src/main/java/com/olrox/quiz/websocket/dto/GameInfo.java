package com.olrox.quiz.websocket.dto;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;

public class GameInfo {
    private Long id;
    private User creator;
    private Integer timeForQuestionInSeconds;
    private Integer numberOfQuestions;
    private SoloGame.Status status;

    public static GameInfo from(SoloGame soloGame) {
        GameInfo gameInfo = new GameInfo();
        gameInfo.id = soloGame.getId();
        gameInfo.creator = soloGame.getCreator();
        gameInfo.status = soloGame.getStatus();
        gameInfo.timeForQuestionInSeconds = soloGame.getTimeForQuestionInSeconds();
        gameInfo.numberOfQuestions = soloGame.getNumberOfQuestions();

        return gameInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Integer getTimeForQuestionInSeconds() {
        return timeForQuestionInSeconds;
    }

    public void setTimeForQuestionInSeconds(Integer timeForQuestionInSeconds) {
        this.timeForQuestionInSeconds = timeForQuestionInSeconds;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public SoloGame.Status getStatus() {
        return status;
    }

    public void setStatus(SoloGame.Status status) {
        this.status = status;
    }
}
