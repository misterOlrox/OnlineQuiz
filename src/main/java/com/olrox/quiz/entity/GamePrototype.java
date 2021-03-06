package com.olrox.quiz.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "solo_game_prototype_table")
public class GamePrototype {
    public enum Type {
        SOLO_RANDOM,
        SOLO_SHARED_PRIVATE,
        SOLO_SHARED_PUBLIC
    }

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

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private String name;

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

    public List<QuizQuestion> getShuffledQuestionList() {
        var listCopy = new ArrayList<>(questionList);

        Collections.shuffle(listCopy);
        return listCopy;
    }

    public void setQuestionList(List<QuizQuestion> questionList) {
        this.questionList = questionList;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SoloGamePrototype{" +
                "id=" + id +
                ", name=" + name +
                ", type=" + type +
                ", timeForQuestionInSeconds=" + timeForQuestionInSeconds +
                ", numberOfQuestions=" + numberOfQuestions +
                '}';
    }
}
