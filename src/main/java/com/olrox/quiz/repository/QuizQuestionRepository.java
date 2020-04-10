package com.olrox.quiz.repository;

import com.olrox.quiz.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    //@Query("select q.id from QuizQuestion q join q.themes theme")
    //List<Long> getQuestionIdsByThemes(Collection<QuizQuestionTheme> themes);
}
