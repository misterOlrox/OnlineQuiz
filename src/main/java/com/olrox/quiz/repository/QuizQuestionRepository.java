package com.olrox.quiz.repository;

import com.olrox.quiz.entity.QuizQuestion;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.projection.UserStatProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    Long countAllByAuthorEquals(User author);

    @Query("select q.id " +
            " from QuizQuestion q join q.themes t" +
            " where t.id in :themesIds and q.approved=true")
    Set<Long> getApprovedQuestionIdsByThemes(@Param("themesIds") List<Long> themesIds);

    @Query("select q from QuizQuestion q join q.themes t where t.id=:themeId")
    List<QuizQuestion> findAllByThemeId(@Param("themeId") Long themeId);

    @Query("select q" +
            " from QuizQuestion q " +
            " where q.approved=false " +
            "   and q.id not in (select q.id from q.approvals approval where approval=:user)")
    List<QuizQuestion> findUnapprovedQuestionsFor(@Param("user") User user);

    @Query("select q from QuizQuestion q join q.themes t where t.id=:themeId")
    Page<QuizQuestion> findAllByThemeId(@Param("themeId") Long themeId, Pageable pageable);

    @Query("select q from QuizQuestion q where q.author.id=:userId")
    Page<QuizQuestion> findPrivateQuestions(@Param("userId") Long userId, Pageable pageable);

    @Query("select u as user, count(u) as value, 'QUESTIONS_ADDED' as type" +
            " from User u join QuizQuestion qq on u=qq.author" +
            " group by u" +
            " order by count(u) DESC")
    Page<UserStatProjection> findTopByQuestionsAdded(Pageable pageable);
}
