package com.olrox.quiz.controller;

import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.service.QuizQuestionThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SetupGameController {

    @Autowired
    private QuizQuestionThemeService quizQuestionThemeService;

    @GetMapping("/setup/solo")
    public String getSetupSolo(Model model) {
        List<QuizQuestionTheme> themes = quizQuestionThemeService.getAllThemes();
        model.addAttribute("themes", themes);

        return "setup/solo";
    }

    @PostMapping("/setup/solo")
    public String postSetupSolo(
            @RequestParam String time,
            @RequestParam String quantity,
            @RequestParam List<String> selectedThemes,
            Model model) {

        boolean hasErrors = false;
        if (StringUtils.isEmpty(time)) {
            model.addAttribute("timeError", "Choose time");
            hasErrors = true;
        }

        int quantityOfQuestions = 0;
        try {
            quantityOfQuestions = Integer.parseInt(quantity);
            if (quantityOfQuestions < 5 || quantityOfQuestions > 100) {
                model.addAttribute("quantityError", "Quantity should be between 5 and 100");
                hasErrors = true;
            }
        } catch (NumberFormatException ex) {
            model.addAttribute("quantityError", "Quantity should be between 5 and 100");
            hasErrors = true;
        }

        if (selectedThemes.isEmpty()) {
            model.addAttribute("selectedThemesError", "Choose at least one theme");
            hasErrors = true;
        }

        if (hasErrors) {
            List<QuizQuestionTheme> themes = quizQuestionThemeService.getAllThemes();
            model.addAttribute("themes", themes);
            return "setup/solo";
        } else {
            return "redirect:/play/solo";
        }
    }
}
