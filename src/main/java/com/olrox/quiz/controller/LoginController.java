package com.olrox.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("loginError", "Invalid username or password!");
        }

        if (logout != null) {
            model.addObject("loggedOutMsg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;
    }
}
