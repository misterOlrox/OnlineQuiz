package com.olrox.quiz.controller.general.auth;

import com.olrox.quiz.controller.util.ControllerUtils;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class SignUpController {

    private final UserService userService;

    @Autowired
    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String addUser(
            @RequestParam("passwordRepeat") String passwordConfirmation,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        boolean anyError = false;
        final boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirmation);

        if (isConfirmEmpty) {
            model.addAttribute("passwordRepeatError", "Password confirmation can't be empty");
            anyError = true;
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)) {
            model.addAttribute("passwordError", "Passwords are not equal!");
            anyError = true;
        }
        if (userService.isUsernameBusy(user.getUsername())) {
            model.addAttribute("usernameError", "Username is busy!");
            anyError = true;
        }
        if (anyError || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);

            return "sign-up";
        }
        if (userService.signUp(user.getUsername(), user.getPassword()) == null) {
            model.addAttribute("usernameError", "Username is busy!");
            return "sign-up";
        }

        model.addAttribute("signUpSuccessful", true);

        return "/login";
    }
}
