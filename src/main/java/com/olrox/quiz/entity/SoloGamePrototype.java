package com.olrox.quiz.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "solo_game_prototype_table")
public class SoloGamePrototype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User creator;

    @OneToMany(mappedBy = "prototype", cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<SoloGame> soloGames = new HashSet<>();

    @ManyToMany
    private List<QuizQuestion> questionList = new ArrayList<>();

    private Integer timeForQuestionInSeconds;
    private Integer numberOfQuestions;

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

    public Set<SoloGame> getSoloGames() {
        return soloGames;
    }

    public void setSoloGames(Set<SoloGame> soloGames) {
        this.soloGames = soloGames;
    }

    public List<QuizQuestion> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuizQuestion> questionList) {
        this.questionList = questionList;
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

    @Override
    public String toString() {
        return "SoloGamePrototype{" +
                "id=" + id +
                ", soloGames=" + soloGames +
                ", questionList=" + questionList +
                ", timeForQuestionInSeconds=" + timeForQuestionInSeconds +
                ", numberOfQuestions=" + numberOfQuestions +
                '}';
    }
}
