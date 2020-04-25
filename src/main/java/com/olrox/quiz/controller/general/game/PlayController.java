package com.olrox.quiz.controller.general.game;

import com.olrox.quiz.controller.general.MyErrorController;
import com.olrox.quiz.entity.Invite;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.exception.IllegalGamePrototypeType;
import com.olrox.quiz.exception.InviteException;
import com.olrox.quiz.process.SoloGameProcess;
import com.olrox.quiz.service.InviteService;
import com.olrox.quiz.service.SoloGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class PlayController {

    public static final Logger LOG = LoggerFactory.getLogger(PlayController.class);

    @Autowired
    private SoloGameService soloGameService;
    @Autowired
    private MyErrorController errorController;
    @Autowired
    private InviteService inviteService;

    @GetMapping("/play/solo/{gameId}")
    public String playSolo(@PathVariable Long gameId,
                           @AuthenticationPrincipal User user,
                           Model model) {
        SoloGameProcess soloGameProcess = soloGameService.getGameProcessById(gameId);
        if (soloGameProcess == null) {
            SoloGame finishedGame = soloGameService.findFinishedGame(gameId).orElse(null);
            if (finishedGame != null) {
                return "redirect:/result/solo/" + gameId;
            } else {
                return errorController.getErrorView(model, "No such game process");
            }
        }
        if (soloGameProcess.isFinished() || soloGameProcess.getCurrentQuestion() == null) {
            soloGameService.finishGame(soloGameProcess);
            return "redirect:/result/solo/" + gameId;
        }
        if (!soloGameProcess.getSoloGame().getParticipant().equals(user)) {
            return errorController.getErrorView(model, "This game isn't yours");
        }

        model.addAttribute("gameId", gameId);
        model.addAttribute("numberOfQuestions", soloGameProcess.getQuestionsCount());

        return "play/solo";
    }

    @GetMapping("play/public-shared/{prototypeId}")
    public String playShared(@PathVariable Long prototypeId,
                             @AuthenticationPrincipal User user,
                             Model model) {
        var alreadyInProgress = soloGameService.findGameAlreadyInProgress(user, prototypeId);
        Long gameId;
        if (alreadyInProgress.isPresent()) {
            gameId = alreadyInProgress.get().getId();
        } else {
            try {
                gameId = soloGameService.generatePublicSharedSoloGame(user, prototypeId);
            } catch (IllegalGamePrototypeType e) {
                return errorController.getErrorView(
                        model, "You don't have access to this quiz");
            }
        }

        return playSolo(gameId, user, model);
    }

    @GetMapping("play/invite/{inviteId}")
    public String playGameFromInvite(@PathVariable Long inviteId,
                                     @AuthenticationPrincipal User user,
                                     Model model) {
        Invite invite;
        try {
            invite = inviteService.getInvite(inviteId, user);
        } catch (InviteException e) {
            return errorController.getErrorView(model, "Access denied");
        }

        Long gameId;
        switch (invite.getStatus()) {
            case IN_PROCESS:
                gameId = soloGameService.generateGameFromInvite(invite);
                break;
            case ACCEPTED:
                long prototypeId = invite.getGamePrototype().getId();
                Optional<SoloGame> optionalGameInProgress =
                        soloGameService.findGameAlreadyInProgress(user, prototypeId);
                if (optionalGameInProgress.isEmpty()) {
                    LOG.error("Game not found for ACCEPTED invite [{}]", invite);
                    return errorController.getErrorView(model, "Game not found");
                }
                gameId = optionalGameInProgress.get().getId();
                break;
            default:
                LOG.error("Invite [{}] has status different from IN_PROGRESS or ACCEPTED", invite);
                return errorController.getErrorView(model, "Game not found");
        }

        return playSolo(gameId, user, model);

    }

}
