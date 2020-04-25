package com.olrox.quiz.dto;

import com.olrox.quiz.entity.Invite;
import com.olrox.quiz.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InviteDto {
    private long id;
    private UserDto sender;
    private UserDto invited;
    private GamePrototypeDto gamePrototype;
    private Long creationTime;
    private Invite.Status status;

    public static InviteDto from(Invite invite) {
        return new InviteDto(
                invite.getId(),
                UserDto.from(invite.getSender()),
                UserDto.from(invite.getInvited()),
                GamePrototypeDto.from(invite.getGamePrototype()),
                TimeUtil.getMillis(invite.getCreationDate()),
                invite.getStatus()
        );
    }
}
