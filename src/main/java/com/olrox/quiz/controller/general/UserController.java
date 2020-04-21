package com.olrox.quiz.controller.general;

import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.GamePrototypeService;
import com.olrox.quiz.service.SoloGameService;
import com.olrox.quiz.service.UserService;
import com.olrox.quiz.service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SoloGameService soloGameService;
    @Autowired
    private GamePrototypeService gamePrototypeService;
    @Autowired
    private UserStatsService userStatsService;
    @Autowired
    private MyErrorController errorController;

    @GetMapping("/profile/{userId}")
    public String getProfile(@AuthenticationPrincipal User user,
                             @PathVariable Long userId,
                             Model model) {
        if (user.getId().equals(userId)) {
            return getProfileByDefault(user, model);
        }

        var optionalUser = userService.findUserById(userId);
        if (optionalUser.isEmpty()) {
            return errorController.getErrorView(model, "User doesn't exists");
        }

        return getProfileWithStats(optionalUser.get(), model);
    }

    @GetMapping("/profile")
    public String getProfileByDefault(@AuthenticationPrincipal User user, Model model) {
        return getProfileWithStats(user, model);
    }

    private String getProfileWithStats(User profileOwner, Model model) {
        model.addAttribute("profileOwner", profileOwner);
        model.addAttribute("quizPlayed",
                userStatsService.getQuizPlayedCount(profileOwner));
        model.addAttribute("quizCreated",
                userStatsService.getQuizCreatedCount(profileOwner));
        model.addAttribute("questionsAdded",
                userStatsService.getQuestionsAddedCount(profileOwner));
        model.addAttribute("correctAnswers",
                userStatsService.getCorrectAnswersCount(profileOwner));

        return "/user/profile";
    }

    @GetMapping("/games")
    public String getGames(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("games", soloGameService.findGamesByParticipant(user));

        return "user/games";
    }

    @GetMapping("/shared")
    public String getShared(Model model, @AuthenticationPrincipal User user) {
        var prototypes = gamePrototypeService.findSharedPrototypesByCreator(user);
        model.addAttribute("prototypes", prototypes);

        return "user/shared";
    }
}
