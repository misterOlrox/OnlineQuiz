package com.olrox.quiz.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class SoloGameController {

    public static final Logger LOG = LoggerFactory.getLogger(InfoController.class);

//    @MessageMapping("/solo.sendAnswer")
//    @SendTo("/topic/tracker")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }
}
