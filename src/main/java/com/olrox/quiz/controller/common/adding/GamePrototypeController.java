package com.olrox.quiz.controller.common.adding;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.GamePrototypeService;
import com.olrox.quiz.service.QuizQuestionService;
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
public class GamePrototypeController {

    public static final Logger LOG = LoggerFactory.getLogger(GamePrototypeController.class);

    @Autowired
    private GamePrototypeService gamePrototypeService;
    @Autowired
    private QuizQuestionService quizQuestionService;

    @GetMapping("/add-prototype")
    public String getFormForAddPrototype() {

        return "/adding/add-prototype";
    }

    // TODO return choice back in model
    @PostMapping("/add-prototype")
    public String postFormForAddPrototype(Model model,
                                          @AuthenticationPrincipal User user,
                                          @RequestParam String prototypeName,
                                          @RequestParam String time,
                                          @RequestParam(defaultValue = "false") boolean isPrivate,
                                          @RequestParam List<Long> selectedQuestions) {

        LOG.debug("User [{}] tried to add new {} game prototype with questions {}",
                user.getUsername(),
                isPrivate ? "private" : "public",
                selectedQuestions.toString()
        );

        boolean hasErrors = false;
        if (StringUtils.isEmpty(time)) {
            model.addAttribute("timeError", "Choose time");
            hasErrors = true;
        }
        if (selectedQuestions.isEmpty()) {
            model.addAttribute("selectedQuestionsError", "Choose questions");
            hasErrors = true;
        }
        if (StringUtils.isEmpty(prototypeName)) {
            model.addAttribute("prototypeNameError", "Quiz name shouldn't be empty");
            hasErrors = true;
        }

        GamePrototype.Type type;
        if (isPrivate) {
            type = GamePrototype.Type.SOLO_SHARED_PRIVATE;
            model.addAttribute("isPrivate", true);
        } else {
            type = GamePrototype.Type.SOLO_SHARED_PUBLIC;
        }

        if (hasErrors) {
            model.addAttribute("prototypeName", prototypeName);
            return "adding/add-prototype";
        } else {
            gamePrototypeService.createPrototype(
                    user,
                    type,
                    prototypeName,
                    quizQuestionService.findAllById(selectedQuestions),
                    TimeUtil.getTimeInSecondsFromStringWithMinuteAndSecond(time)
            );
            return "redirect:/user/shared";
        }
    }
}
