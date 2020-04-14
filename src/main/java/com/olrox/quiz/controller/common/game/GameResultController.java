package com.olrox.quiz.controller.common.game;

import com.olrox.quiz.controller.common.MyErrorController;
import com.olrox.quiz.entity.SoloGameResult;
import com.olrox.quiz.service.SoloGameResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class GameResultController {

    @Autowired
    private SoloGameResultService soloGameResultService;
    @Autowired
    private MyErrorController myErrorController;

    @GetMapping("/result/solo/{id}")
    public String getResultSolo(@PathVariable Long id, Model model) {
        Optional<SoloGameResult> optional = soloGameResultService.find(id);

        if (optional.isEmpty()) {
            return myErrorController.getErrorView(model, "There isn't any results with id: " + id);
        }

        SoloGameResult result = optional.get();
        model.addAttribute("result", result);
        model.addAttribute("game", result.getParent());

        return "result/solo";
    }
}
