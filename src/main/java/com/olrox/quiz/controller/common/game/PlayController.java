package com.olrox.quiz.controller.common.game;

import com.olrox.quiz.controller.common.MyErrorController;
import com.olrox.quiz.process.SoloGameProcess;
import com.olrox.quiz.service.SoloGameResultService;
import com.olrox.quiz.service.SoloGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PlayController {

    @Autowired
    private SoloGameService soloGameService;
    @Autowired
    private SoloGameResultService soloGameResultService;
    @Autowired
    private MyErrorController errorController;

    @GetMapping("/play/solo/{id}")
    public String playSolo(@PathVariable Long id, Model model) {
        SoloGameProcess soloGameProcess = soloGameService.getGameProcessById(id);
        if (soloGameProcess == null) {
            Long resultId = soloGameResultService.getResultIdBySoloGameId(id);
            if (resultId != null) {
                return "redirect:/result/solo/" + id;
            } else {
                return errorController.getErrorView(model, "No such game process");
            }
        }
        if (soloGameProcess.isFinished()) {
            var result = soloGameService.finishGame(soloGameProcess);
            return "redirect:/result/solo/" + result.getId();
        }

        model.addAttribute("gameId", id);

        return "play/solo";
    }

}
