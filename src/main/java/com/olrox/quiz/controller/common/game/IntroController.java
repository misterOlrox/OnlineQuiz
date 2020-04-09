package com.olrox.quiz.controller.common.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/intro")
public class IntroController {

    @GetMapping("/solo")
    public String soloQuizIntro() {
        return "intro/solo";
    }
}
