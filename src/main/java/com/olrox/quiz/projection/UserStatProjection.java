package com.olrox.quiz.projection;


import com.olrox.quiz.entity.User;

public interface UserStatProjection {
    User getUser();
    String getValue();
    UserStatType getType();
}
