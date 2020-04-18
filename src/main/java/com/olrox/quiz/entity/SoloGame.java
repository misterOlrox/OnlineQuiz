package com.olrox.quiz.entity;

import com.olrox.quiz.util.TimeUtil;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "solo_game_table")
public class SoloGame {
    public enum Status {
        IN_PROGRESS,
        FINISHED,
        INTERRUPTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prototype_id", nullable = false)
    private SoloGamePrototype prototype;

    @ManyToOne
    private User participant;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime creationTime;
    private LocalDateTime finishTime;
    private int correctAnswersCount = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SoloGamePrototype getPrototype() {
        return prototype;
    }

    public void setPrototype(SoloGamePrototype prototype) {
        this.prototype = prototype;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User creator) {
        this.participant = creator;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime started) {
        this.creationTime = started;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public void setCorrectAnswersCount(int correctAnswersCount) {
        this.correctAnswersCount = correctAnswersCount;
    }

    public String getCreationTimeString() {
        return TimeUtil.getStringFrom(creationTime);
    }

    public String getFinishTimeString() {
        return TimeUtil.getStringFrom(finishTime);
    }

    @Override
    public String toString() {
        return "SoloGame{" +
                "id=" + id +
                ", prototypeId=" + prototype.getId() +
                ", participant=" + participant +
                ", status=" + status +
                '}';
    }
}
