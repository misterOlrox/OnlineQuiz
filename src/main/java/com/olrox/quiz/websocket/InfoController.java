package com.olrox.quiz.websocket;

import com.olrox.quiz.dto.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class InfoController {

    public static final Logger LOG = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
