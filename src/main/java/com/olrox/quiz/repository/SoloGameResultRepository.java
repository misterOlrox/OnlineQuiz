package com.olrox.quiz.repository;

import com.olrox.quiz.entity.SoloGameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SoloGameResultRepository extends JpaRepository<SoloGameResult, Long> {

    @Query("select res.id from SoloGameResult res where res.parent.id = :id")
    Long findSoloGameResultByParentId(@Param("id") Long id);
}
