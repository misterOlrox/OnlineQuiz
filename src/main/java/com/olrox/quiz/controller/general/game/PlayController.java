package com.olrox.quiz.controller.general.game;

import com.olrox.quiz.controller.general.MyErrorController;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.process.SoloGameProcess;
import com.olrox.quiz.service.SoloGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PlayController {

    @Autowired
    private SoloGameService soloGameService;
    @Autowired
    private MyErrorController errorController;

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
            gameId = soloGameService.generateSharedSoloGame(user, prototypeId);
        }

        return playSolo(gameId, user, model);
    }

}
