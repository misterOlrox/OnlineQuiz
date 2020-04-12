package com.olrox.quiz.controller.common;

import com.olrox.quiz.service.QuizQuestionThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThemeController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ThemeController.class);

    @Autowired private QuizQuestionThemeService quizQuestionThemeService;


    @GetMapping("/add-theme")
    public String addTheme() {
        return "add-theme";
    }

    @PostMapping("/add-theme")
    public String postAddTheme(
            @RequestParam String themeName,
            Model model
    ) {

        boolean anyErrors = false;
        if (StringUtils.isEmpty(themeName)) {
            model.addAttribute("themeError", "Wrong name");
            anyErrors = true;
        }

        if (quizQuestionThemeService.exists(themeName)) {
            model.addAttribute("themeError", "Theme already exists");
            anyErrors = true;
        }

        if (!anyErrors) {
            quizQuestionThemeService.add(themeName);
            model.addAttribute("success", "Theme successfully added");
            LOGGER.info("Theme '{}' was added", themeName);
        } else {
            LOGGER.warn("Theme error: {}", model.getAttribute("themeError"));
        }

        return "/add-theme";
    }
}
