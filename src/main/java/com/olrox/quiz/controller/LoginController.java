package com.olrox.quiz.controller;

import com.olrox.quiz.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("signUpSuccessful", true);
        }

        return "login";
    }

    @PostMapping("/login")
    public String loginAsUser() {


        return "redirect:/login";
    }
}
