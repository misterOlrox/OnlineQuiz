package com.olrox.quiz.repository;

import com.olrox.quiz.entity.SoloGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoloGameRepository extends JpaRepository<SoloGame, Long> {
}
