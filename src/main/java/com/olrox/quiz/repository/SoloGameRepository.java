package com.olrox.quiz.repository;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SoloGameRepository extends JpaRepository<SoloGame, Long> {

    List<SoloGame> findAllByParticipant(User participant);

//    @Query("select game from SoloGame game join fetch SoloGamePrototype where :participant = game.participant")
//    List<SoloGame> findAllByParticipant(@Param("participant") User participant);

    List<SoloGame> findAllByStatusEquals(SoloGame.Status status);

    List<SoloGame> deleteAllByStatusEquals(SoloGame.Status status);

    Optional<SoloGame> findFirstByIdEqualsAndStatusEquals(Long id, SoloGame.Status status);
}
