package com.olrox.quiz.repository;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.projection.UserStatProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface GamePrototypeRepository extends JpaRepository<GamePrototype, Long> {

    List<GamePrototype> findAllByCreatorEqualsOrderById(User creator);

    Long countAllByCreatorEqualsAndTypeIn(User creator, Set<GamePrototype.Type> types);

    @Query("select u as user, count(u) as value, 'QUIZ_CREATED' as type" +
            " from User u join GamePrototype gp on u.id = gp.creator.id " +
            " where gp.type in (:types)" +
            " group by u" +
            " order by count(u) DESC")
    Page<UserStatProjection> findTopByQuizCreated(Pageable pageable,@Param("types") Set<GamePrototype.Type> types);

    @Query("select gp" +
            " from GamePrototype gp " +
            " where " +
            " gp.creator.id=:creatorId " +
            "    and (gp.type = 'SOLO_SHARED_PUBLIC' " +
            "        or gp.type = 'SOLO_SHARED_PRIVATE') " +
            " order by gp.id")
    List<GamePrototype> findAllSharedByCreator(@Param("creatorId") Long creatorId);

    @Query("select gp from GamePrototype gp where gp.type = 'SOLO_SHARED_PUBLIC'")
    Set<GamePrototype> findAllPublic();
}
