package com.olrox.quiz.controller.common;

import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.QuizQuestionService;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class AddQuestionController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AddQuestionController.class);

    @Autowired
    private QuizQuestionService quizQuestionService;


    @GetMapping("/add-question")
    public String addQuestion() {
        return "add-question";
    }

    @PostMapping("/add-question")
    public String addQuestionByUser(
            @RequestParam String question,
            @RequestParam String correctAnswer,
            @RequestParam List<String> wrongAnswers,
            @AuthenticationPrincipal User user,
            Model model
    ) {

        boolean anyError = false;

        if (StringUtils.isEmpty(question)) {
            model.addAttribute("questionError", "Question can't be empty");
            anyError = true;
        }

        if (StringUtils.isEmpty(correctAnswer)) {
            model.addAttribute("correctError", "Answer can't be empty");
            anyError = true;
        }

        if (wrongAnswers == null
                || wrongAnswers.isEmpty()
                || StringUtils.isEmpty(wrongAnswers.get(0))) {
            model.addAttribute("wrongError", "Fill wrong answers");
            anyError = true;
        }

        if (anyError) {
            LOGGER.info("Something was wrong in form filling");
            return "add-question";
        } else {
            LOGGER.info("Success add-question form filling");

            List<String> filtered = new ArrayList<>();
            for (String wrongAnswer : wrongAnswers) {
                if (!StringUtils.isEmpty(wrongAnswer)) {
                    filtered.add(wrongAnswer);
                }
            }
            quizQuestionService.addQuestion(user, question, correctAnswer, filtered);
            model.addAttribute("success", "Successfully added new question");

            return "add-question";
        }
    }
}
