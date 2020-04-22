package com.olrox.quiz.service.sender;

import com.olrox.quiz.dto.InviteDto;
import com.olrox.quiz.entity.Invite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class InviteSender {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void send(Invite invite) {
        simpMessagingTemplate.convertAndSendToUser(
                invite.getInvited().getUsername(),
                "/invites",
                InviteDto.from(invite)
        );
    }
}
