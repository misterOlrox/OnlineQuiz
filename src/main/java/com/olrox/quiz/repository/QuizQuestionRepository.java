package com.olrox.quiz.repository;

import com.olrox.quiz.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    @Query("select q.id from QuizQuestion q join q.themes t where t.id in :themesIds")
    Set<Long> getQuestionIdsByThemes(@Param("themesIds") List<Long> themesIds);

}
