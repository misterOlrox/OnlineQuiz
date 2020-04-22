package com.olrox.quiz.dto;

import com.olrox.quiz.entity.Invite;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InviteDto {
    private UserDto sender;
    private UserDto invited;
    private Long gamePrototypeId;
    private Invite.Status status;

    public static InviteDto from(Invite invite) {
        return new InviteDto(
                UserDto.from(invite.getSender()),
                UserDto.from(invite.getInvited()),
                invite.getGamePrototype().getId(),
                invite.getStatus()
        );
    }
}
