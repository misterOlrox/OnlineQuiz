package com.olrox.quiz.projection;

import java.util.HashMap;
import java.util.Map;

public enum UserStatType {
    QUIZ_PLAYED("quiz-played", "quiz played"),
    QUIZ_CREATED("quiz-created", "quiz created"),
    QUESTIONS_ADDED("questions-added", "questions added"),
    CORRECT_ANSWERS("correct-answers", "correct answers");

    private static Map<String, UserStatType> pathMap = new HashMap<>();

    static {
        for(UserStatType type : UserStatType.values()) {
            pathMap.put(type.path, type);
        }
    }

    private String path;
    private String name;

    UserStatType(String statPath, String name) {
        this.path = statPath;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public static UserStatType getByPath(String path) {
        return pathMap.get(path);
    }
}