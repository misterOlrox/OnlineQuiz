package com.olrox.quiz.dto;

import lombok.Data;

import java.util.List;

@Data
public class InviteRequestDto {
    private List<Long> invitedUsersIds;
}
