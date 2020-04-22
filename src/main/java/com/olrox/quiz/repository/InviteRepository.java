package com.olrox.quiz.repository;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.Invite;
import com.olrox.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    Invite findFirstByGamePrototypeAndSenderAndInvited(GamePrototype prototype, User sender, User invited);

    List<Invite> findAllByInvitedEqualsAndStatusEquals(User invited, Invite.Status status);
}
