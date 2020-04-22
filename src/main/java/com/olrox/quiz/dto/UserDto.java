package com.olrox.quiz.dto;

import com.olrox.quiz.entity.User;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;

    public static UserDto from(User user) {
        var dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());

        return dto;
    }
}
