package com.olrox.quiz.controller.common.game;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PlayController {

    @GetMapping("/play/solo/{id}")
    public String playSolo(@PathVariable Long id, Model model) {
        model.addAttribute("gameId", id);

        return "play/solo";
    }

}
