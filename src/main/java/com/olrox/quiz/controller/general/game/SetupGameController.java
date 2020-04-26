package com.olrox.quiz.controller.general.game;

import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.exception.NoQuestionsException;
import com.olrox.quiz.service.QuizQuestionThemeService;
import com.olrox.quiz.service.SoloGameService;
import com.olrox.quiz.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SetupGameController {

    public static final Logger LOG = LoggerFactory.getLogger(SetupGameController.class);

    @Autowired
    private QuizQuestionThemeService quizQuestionThemeService;
    @Autowired
    private SoloGameService soloGameService;

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
            @RequestParam List<Long> selectedThemes,
            @AuthenticationPrincipal User user,
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
            return getSetupSolo(model);
        } else {

            Long soloGame;
            try {
                soloGame = soloGameService.generateRandomSoloGame(
                        user,
                        TimeUtil.getTimeInSecondsFromStringWithMinuteAndSecond(time),
                        quantityOfQuestions,
                        selectedThemes);
            } catch (NoQuestionsException e) {
                model.addAttribute(
                        "selectedThemesError",
                        "No approved questions for selected themes"
                );
                return getSetupSolo(model);
            }

            return "redirect:/play/solo/" + soloGame;
        }
    }
}
