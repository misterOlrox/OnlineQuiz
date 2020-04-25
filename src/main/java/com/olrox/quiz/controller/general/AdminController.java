package com.olrox.quiz.controller.general;

import com.olrox.quiz.controller.util.ControllerUtils;
import com.olrox.quiz.entity.Role;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-list")
    public String getUserList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "admin/user-list";
    }

    @GetMapping("/user-edit/{userId}")
    public String getUserEditForm(@PathVariable long userId, Model model) {
        model.addAttribute("user", userService.findUserById(userId).get());
        model.addAttribute("roles", Role.values());

        return "admin/user-edit";
    }

    @PostMapping("/user-edit")
    public String postUserEditForm(@RequestParam("userId") User user,
                                   @RequestParam Map<String, String> params,
                                   Model model) {
        var selectedRoles = ControllerUtils.extractRoles(params);
        user = userService.updateRoles(user, selectedRoles);

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("success", true);

        return "admin/user-edit";
    }
}
