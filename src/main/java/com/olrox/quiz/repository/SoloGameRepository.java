package com.olrox.quiz.repository;

import com.olrox.quiz.entity.SoloGame;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.projection.UserStatProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SoloGameRepository extends JpaRepository<SoloGame, Long> {

    List<SoloGame> findAllByParticipant(User participant);

    Long countAllByParticipantEqualsAndStatusEquals(User participant, SoloGame.Status status);

    List<SoloGame> findAllByStatusEquals(SoloGame.Status status);

    List<SoloGame> deleteAllByStatusEquals(SoloGame.Status status);

    Optional<SoloGame> findFirstByIdEqualsAndStatusEquals(Long id, SoloGame.Status status);

    @Query("select sg" +
            " from SoloGame sg" +
            " where " +
            "   sg.prototype.id = :prototypeId" +
            "   and sg.participant.id=:userId" +
            "   and sg.status = 'IN_PROGRESS'" +
            " order by sg.creationTime")
    Optional<SoloGame> findInProgressWithParticipant(@Param("userId") Long userId,
                                                     @Param("prototypeId") Long prototypeId);

    @Query("select u as user, count(u) as value, 'QUIZ_PLAYED' as type " +
            " from SoloGame sg join User u on u.id=sg.participant.id" +
            " where sg.status = 'FINISHED'" +
            " group by u.id" +
            " order by count(u) DESC")
    Page<UserStatProjection> findTopByQuizPlayed(Pageable pageable);
}
