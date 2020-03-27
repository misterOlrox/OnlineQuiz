package com.olrox.quiz.controller;

import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class SubscribeController {
    private final UserService userService;

    public SubscribeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ){
        userService.subscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ){
        userService.unsubscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(
            Model model,
            @PathVariable User user,
            @PathVariable String type
    ){
        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);

        if("subscriptions".equals(type)){
            model.addAttribute("users", user.getSubscriptions());
        } else{
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }
}
