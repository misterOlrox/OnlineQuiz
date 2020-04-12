package com.olrox.quiz.repository;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoloGameRepository extends JpaRepository<SoloGame, Long> {

    List<SoloGame> findAllByCreator(User creator);

    List<SoloGame> findAllByStatusEquals(SoloGame.Status status);

    List<SoloGame> deleteAllByStatusEquals(SoloGame.Status status);
}
