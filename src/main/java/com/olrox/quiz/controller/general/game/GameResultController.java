package com.olrox.quiz.controller.general.game;

import com.olrox.quiz.controller.general.MyErrorController;
import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.service.SoloGameService;
import com.olrox.quiz.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class GameResultController {

    @Autowired
    private SoloGameService soloGameService;
    @Autowired
    private UserAnswerService userAnswerService;
    @Autowired
    private MyErrorController myErrorController;

    @GetMapping("/result/solo/{id}")
    public String getResultSolo(@PathVariable Long id, Model model) {
        Optional<SoloGame> optional = soloGameService.findFinishedGame(id);

        if (optional.isEmpty()) {
            return myErrorController.getErrorView(model, "There isn't any results with id: " + id);
        }

        SoloGame finishedGame = optional.get();
        var userAnswerList = userAnswerService.findUserAnswers(finishedGame);
        model.addAttribute("finishedGame", finishedGame);
        model.addAttribute("prototype", finishedGame.getPrototype());
        model.addAttribute("userAnswerList", userAnswerList);

        return "result/solo";
    }
}
