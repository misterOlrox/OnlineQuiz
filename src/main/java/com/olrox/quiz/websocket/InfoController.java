package com.olrox.quiz.websocket;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.SoloGameService;
import com.olrox.quiz.websocket.dto.ChatMessage;
import com.olrox.quiz.websocket.dto.GameInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
public class InfoController {

    public static final Logger LOG = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SoloGameService soloGameService;

    @SubscribeMapping("/topic/info.game/{id}")
    public GameInfo gameInfo(@DestinationVariable Long id, @AuthenticationPrincipal User user) {
        SoloGame soloGame = soloGameService.getGameById(id);

        if (!soloGame.getCreator().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("Access denied for game with id " + soloGame.getId());
        }

        return GameInfo.from(soloGame);
    }

//    @MessageMapping("/topic/info.game/{id}")
//    @SendTo("/topic/info.game/{id}")
//    public GameInfo sendInfo(@Payload GameInfo info) {
//        return info;
//    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/tracker")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/tracker")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageExceptionHandler
    @SendToUser(destinations="/topic/errors", broadcast=false)
    public ChatMessage handleException(Exception exception) {
        LOG.error("Exception occurred.", exception);

        var msg = new ChatMessage();
        msg.setContent(exception.toString());
        msg.setType(ChatMessage.MessageType.CHAT);
        msg.setSender("Server");
        return msg;
    }
}

