package com.olrox.quiz.controller.common.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlayController {

    @GetMapping("/play/solo")
    public String playSolo() {


        return "play/solo";
    }

}
