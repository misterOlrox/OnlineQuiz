package com.olrox.quiz.util;

import com.olrox.quiz.entity.QuizQuestionTheme;
import com.olrox.quiz.entity.Role;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.WrongAnswer;
import com.olrox.quiz.service.InviteService;
import com.olrox.quiz.service.QuizQuestionService;
import com.olrox.quiz.service.QuizQuestionThemeService;
import com.olrox.quiz.service.SoloGameService;
import com.olrox.quiz.service.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AppStartJobs {

    private static final Logger LOG = LoggerFactory.getLogger(AppStartJobs.class);

    @Autowired
    private SoloGameService soloGameService;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuizQuestionThemeService quizQuestionThemeService;
    @Autowired
    private QuizQuestionService quizQuestionService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void firstStartup() throws IOException {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        if (!quizQuestionService.findAllById(list).isEmpty()) {
            LOG.warn("Database already filled with questions");
            return;
        }

        LOG.info("Filling database with questions ...");
        User admin = userService.findAdmin().orElseGet(() -> {
            var alt = userService.signUp("admin", "admin");
            Set<Role> roleSet = new HashSet<>(Arrays.asList(Role.values()));
            userService.updateRoles(alt, roleSet);
            return alt;
        });

        FileReader fileReader = new FileReader("parsed-questions0.txt");
        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        QuizQuestionTheme quizQuestionTheme = new QuizQuestionTheme();
        quizQuestionTheme.setThemeName("Вопросы на русском");
        quizQuestionThemeService.add(quizQuestionTheme);

        int i = 0;
        while (line != null && i <= 1250) {
            JSONObject jsonObject = new JSONObject(line);

            var question = quizQuestionService.addQuestion(
                    admin,
                    jsonObject.getString("question"),
                    jsonObject.getString("correctAnswer"),
                    WrongAnswer.from(jsonObject.getJSONArray("wrongAnswers").toList()),
                    true,
                    quizQuestionTheme
            );

            LOG.debug("Added {}", question);
            line = reader.readLine();
            i++;
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void clearGamesInProgressFromDb() {
        LOG.info("Clear games ...");
        soloGameService.deleteGamesInProgress();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void clearInactiveInvitesFromDb() {
        LOG.info("Clear invites ...");
        inviteService.deleteInactiveInvites();
    }
}
