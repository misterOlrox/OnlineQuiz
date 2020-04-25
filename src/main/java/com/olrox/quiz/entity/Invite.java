package com.olrox.quiz.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "invite_table")
public class Invite {
    public enum Status {
        IN_PROCESS,
        ACCEPTED,
        DECLINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User sender;

    @OneToOne
    private User invited;

    @OneToOne
    private GamePrototype gamePrototype;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getInvited() {
        return invited;
    }

    public GamePrototype getGamePrototype() {
        return gamePrototype;
    }

    public void setGamePrototype(GamePrototype gamePrototype) {
        this.gamePrototype = gamePrototype;
    }

    public void setInvited(User invited) {
        this.invited = invited;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "id=" + id +
                ", sender=" + sender +
                ", invited=" + invited +
                ", gamePrototype=" + gamePrototype +
                ", status=" + status +
                ", creationDate=" + creationDate +
                '}';
    }
}
