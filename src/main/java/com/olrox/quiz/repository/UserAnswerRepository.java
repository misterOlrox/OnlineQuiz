package com.olrox.quiz.repository;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.UserAnswer;
import com.olrox.quiz.projection.UserStatProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    List<UserAnswer> findAllByGameEquals(SoloGame soloGame);

    @Query("select count(ans)" +
            " from UserAnswer ans " +
            "   join SoloGame game on ans.game = game" +
            " where game.participant=:user " +
            "   and ans.status = 'CORRECT' ")
    Long countCorrectAnswersByUser(@Param("user") User user);

    @Query("select u as user, count(u) as value, 'CORRECT_ANSWERS' as type" +
            " from User u join SoloGame sg on u = sg.participant join UserAnswer answ on answ.game=sg" +
            " where answ.status='CORRECT'" +
            " group by u" +
            " order by count(u) DESC")
    Page<UserStatProjection> findTopByCorrectAnswer(Pageable pageable);
}
