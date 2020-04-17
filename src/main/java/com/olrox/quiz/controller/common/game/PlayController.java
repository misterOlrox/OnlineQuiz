package com.olrox.quiz.controller.common.game;

import com.olrox.quiz.controller.common.MyErrorController;
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

    @GetMapping("/play/solo/{id}")
    public String playSolo(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        SoloGameProcess soloGameProcess = soloGameService.getGameProcessById(id);
        if (soloGameProcess == null) {
            SoloGame finishedGame = soloGameService.findFinishedGame(id).orElse(null);
            if (finishedGame != null) {
                return "redirect:/result/solo/" + id;
            } else {
                return errorController.getErrorView(model, "No such game process");
            }
        }
        if (soloGameProcess.isFinished() || soloGameProcess.getCurrentQuestion() == null) {
            soloGameService.finishGame(soloGameProcess);
            return "redirect:/result/solo/" + id;
        }
        if (!soloGameProcess.getSoloGame().getParticipant().equals(user)) {
            return errorController.getErrorView(model, "This game isn't yours");
        }

        model.addAttribute("gameId", id);

        return "play/solo";
    }

}
