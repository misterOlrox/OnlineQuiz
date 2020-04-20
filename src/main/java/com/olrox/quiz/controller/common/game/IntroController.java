package com.olrox.quiz.controller.common.game;

import com.olrox.quiz.service.GamePrototypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/intro")
public class IntroController {

    @Autowired
    private GamePrototypeService gamePrototypeService;

    @GetMapping("/solo")
    public String soloQuizIntro() {
        return "intro/solo";
    }

    @GetMapping("/shared-public")
    public String getSharedPublicQuizIntro(Model model) {
        var publicPrototypes = gamePrototypeService.findAllPublicPrototypes();
        model.addAttribute("prototypes", publicPrototypes);

        return "intro/shared-public";
    }
}
