package com.olrox.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class OnlineUsersTopicInfo implements InfoDto {
    private final String type = "online.topic.info.all";
    private Set<UserDto> onlineUsers;

    @Override
    public String getType() {
        return type;
    }
}
