package com.olrox.quiz.controller.general;

import com.olrox.quiz.projection.UserStatType;
import com.olrox.quiz.service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private UserStatsService userStatsService;
    @Autowired
    private MyErrorController errorController;

    @GetMapping("/{statPath}")
    public String getTopQuizPlayed(@PathVariable String statPath,
                                   Model model) {
        UserStatType statType = UserStatType.getByPath(statPath);
        if (statType == null) {
            return getNotFoundErrorView(statPath, model);
        } else {
            var userTop = userStatsService.getTopNUsersByByStat(10, statType);
            if (userTop != null) {
                model.addAttribute("statType", statType.getName());
                model.addAttribute("userStats", userTop.getContent());
            } else {
                return getNotFoundErrorView(statPath, model);
            }
        }

        return "rating";
    }

    private String getNotFoundErrorView(String statPath, Model model) {
        return errorController.getErrorView(
                model, "Rating with stat " + statPath + " doesn't exist");
    }
}
