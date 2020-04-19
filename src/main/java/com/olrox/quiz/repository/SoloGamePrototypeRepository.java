package com.olrox.quiz.repository;

import com.olrox.quiz.entity.GamePrototype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoloGamePrototypeRepository extends JpaRepository<GamePrototype, Long> {

}
