package com.olrox.quiz.controller.general;

import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    @Autowired
    private QuizQuestionService quizQuestionService;

    @GetMapping("/questions-approvals")
    public String getQuestionApprovals(@AuthenticationPrincipal User user,
                                       Model model) {
        model.addAttribute(
                "questions", quizQuestionService.findUnapprovedQuestionsFor(user));


        return "moderator/questions-approvals";
    }

    @GetMapping("/question-approve/{questionId}")
    public String approve(@PathVariable Long questionId,
                          @AuthenticationPrincipal User user,
                          Model model) {
        quizQuestionService.approveQuestion(questionId, user);

        return getQuestionApprovals(user, model);
    }
}
