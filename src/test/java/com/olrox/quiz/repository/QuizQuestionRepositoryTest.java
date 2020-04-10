package com.olrox.quiz.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class QuizQuestionRepositoryTest {

    @Autowired QuizQuestionRepository quizQuestionRepository;

    @Disabled
    @Test
    void getQuestionIdsByThemes() {
        System.out.println(quizQuestionRepository.getQuestionIdsByThemes(List.of(2L, 3L)));
    }
}