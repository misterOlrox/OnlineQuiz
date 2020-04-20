package com.olrox.quiz.repository;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.entity.UserAnswer;
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
}
