package com.olrox.quiz.controller.common;

import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.SoloGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SoloGameService soloGameService;

    @GetMapping("/profile")
    public String getProfile() {
        return "user/profile";
    }

    @GetMapping("/games")
    public String getGames(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("games", soloGameService.findGamesByParticipant(user));

        return "user/games";
    }
}
