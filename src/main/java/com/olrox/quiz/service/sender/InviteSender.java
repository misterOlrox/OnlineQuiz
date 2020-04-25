package com.olrox.quiz.service.sender;

import com.olrox.quiz.dto.InviteDto;
import com.olrox.quiz.entity.Invite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class InviteSender {

    public static final Logger LOG = LoggerFactory.getLogger(InviteSender.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void send(Invite invite) {
        LOG.debug("Sending invite: [{}]", invite);

        simpMessagingTemplate.convertAndSendToUser(
                invite.getInvited().getUsername(),
                "/queue/invites",
                InviteDto.from(invite)
        );
    }
}
