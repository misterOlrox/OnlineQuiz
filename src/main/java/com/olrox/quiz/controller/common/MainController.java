package com.olrox.quiz.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    public MainController() {
    }

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }
}