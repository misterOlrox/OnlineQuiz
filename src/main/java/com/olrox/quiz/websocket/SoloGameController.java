package com.olrox.quiz.websocket;

import com.olrox.quiz.entity.User;
import com.olrox.quiz.process.SoloGameProcess;
import com.olrox.quiz.service.SoloGameService;
import com.olrox.quiz.websocket.dto.GameProcessInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
public class SoloGameController {

    public static final Logger LOG = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SoloGameService soloGameService;

    @SubscribeMapping("/topic/solo.game.info/{id}")
    public GameProcessInfo gameInfo(@DestinationVariable Long id, @AuthenticationPrincipal User user) {
        SoloGameProcess gameProcess = soloGameService.getGameProcessById(id);
        if (!gameProcess.getSoloGame().getCreator().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("Access denied for game with id " + id);
        }

        LOG.info("Sending game {} info to user {}", id, user.getUsername());
        messagingTemplate.convertAndSend("/topic/solo.game.info/" + id, GameProcessInfo.from(gameProcess));

        return GameProcessInfo.from(gameProcess);
    }

    @MessageMapping("/solo.game/{id}/get.question")
    public void sendMessage(@DestinationVariable Long id) {
        var process = soloGameService.getGameProcessById(id);

        LOG.info("Sending question to soloGame with id {}", id);
        messagingTemplate.convertAndSend(
                "/topic/solo.game/" + id,
                process.getCurrentQuestionDto());
    }

}
