package com.olrox.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewOnlineUserInfo implements InfoDto {
    private final String type = "online.topic.info.new";
    private UserDto newUser;

    @Override
    public String getType() {
        return type;
    }
}
