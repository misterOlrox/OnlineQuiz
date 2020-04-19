package com.olrox.quiz.rest;

import com.olrox.quiz.dto.QuestionGeneralInfoDto;
import com.olrox.quiz.dto.ThemeDto;
import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.QuizQuestionService;
import com.olrox.quiz.service.QuizQuestionThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/content")
public class QuizContentRestController {

    @Autowired
    private QuizQuestionThemeService quizQuestionThemeService;
    @Autowired
    private QuizQuestionService quizQuestionService;

    @GetMapping("/available/themes")
    public ResponseEntity<?> getAvailableThemes() {
        return new ResponseEntity<>(
                quizQuestionThemeService
                        .getAllThemes()
                        .stream()
                        .map(ThemeDto::from)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/questions-all")
    public ResponseEntity<?> getQuestions(@RequestParam Long themeId) {
        return new ResponseEntity<>(
                quizQuestionService
                        .findAllByThemeId(themeId)
                        .stream()
                        .map(QuestionGeneralInfoDto::from)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping("/questions")
    public ResponseEntity<Page<?>> getQuestions(
            @RequestParam Long themeId,
            @RequestParam(defaultValue = "false") Boolean isPrivate,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @AuthenticationPrincipal User user
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<QuizQuestion> findedQuestions;
        if (isPrivate) {
            findedQuestions = quizQuestionService.findPrivateQuestions(user, pageable);
        } else {
            findedQuestions = quizQuestionService.findAllByThemeId(themeId, pageable);
        }

        return new ResponseEntity<>(
                findedQuestions.map(QuestionGeneralInfoDto::from),
                HttpStatus.OK
        );
    }

}
