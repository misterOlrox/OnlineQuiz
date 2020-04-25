package com.olrox.quiz.websocket;

import com.olrox.quiz.dto.ChatMessage;
import com.olrox.quiz.dto.InviteDto;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.exception.InviteException;
import com.olrox.quiz.service.InviteService;
import com.olrox.quiz.service.OnlineUserRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {

    public static final Logger LOG = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private OnlineUserRegistry onlineUserRegistry;
    @Autowired
    private InviteService inviteService;

    @SubscribeMapping("/topic/online")
    public void subscribeToOnlineTopic(@AuthenticationPrincipal User user) {
        onlineUserRegistry.addConnected(user);
    }

    @MessageMapping("/queue/invites/all")
    @SendToUser(destinations = "/queue/invites/all", broadcast = false)
    public List<InviteDto> getCurrentInvites(@AuthenticationPrincipal User user) {
        return inviteService
                .getCurrentInvites(user)
                .stream()
                .map(InviteDto::from)
                .collect(Collectors.toList());
    }

    @MessageMapping("/queue/invite/{id}/decline")
    public void decline(@DestinationVariable long id, @AuthenticationPrincipal User user) {
        try {
            inviteService.declineInvite(id, user);
        } catch (InviteException e) {
            LOG.error("InviteException: ", e);
        }
    }

    @MessageExceptionHandler
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public ChatMessage handleException(Exception exception) {
        LOG.error("Exception occurred.", exception);

        var msg = new ChatMessage();
        msg.setContent(exception.toString());
        msg.setType(ChatMessage.MessageType.CHAT);
        msg.setSender("Server");
        return msg;
    }
}

