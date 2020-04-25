package com.olrox.quiz.repository;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.Invite;
import com.olrox.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    Invite findFirstByGamePrototypeAndSenderAndInvitedAndStatus(
            GamePrototype prototype, User sender, User invited, Invite.Status status);

    List<Invite> findAllByInvitedEqualsAndStatusEqualsOrderByCreationDate(
            User invited, Invite.Status status);

    void deleteAllByStatusIn(Set<Invite.Status> statusSet);

    @Query("update Invite i set i.status=:status where i.id=:id")
    void updateInviteStatus(@Param("id")long inviteId, @Param("status")Invite.Status status);
}
