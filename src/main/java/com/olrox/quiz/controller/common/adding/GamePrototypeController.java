package com.olrox.quiz.controller.common.adding;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GamePrototypeController {

    @GetMapping("/add-prototype")
    public String getFormForAddPrototype() {

        return "add-prototype";
    }
}
