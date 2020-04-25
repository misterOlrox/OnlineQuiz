package com.olrox.quiz.service;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.Invite;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.exception.InviteException;
import com.olrox.quiz.repository.GamePrototypeRepository;
import com.olrox.quiz.repository.InviteRepository;
import com.olrox.quiz.service.sender.InviteSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class InviteService {

    public static final Logger LOG = LoggerFactory.getLogger(InviteService.class);

    @Autowired
    private InviteRepository inviteRepository;
    @Autowired
    private GamePrototypeRepository gamePrototypeRepository;
    @Autowired
    private InviteSender inviteSender;

    public Invite addInvite(User sender, User invited, Long prototypeId) {
        var gamePrototypeOptional = gamePrototypeRepository.findById(prototypeId);
        if (gamePrototypeOptional.isEmpty()) {
            LOG.error("Game prototype with id {} doesn't exist", prototypeId);
            return null;
        } else {
            GamePrototype gamePrototype = gamePrototypeOptional.get();
            Invite existingInvite =
                    inviteRepository.findFirstByGamePrototypeAndSenderAndInvitedAndStatus(
                            gamePrototype, sender, invited, Invite.Status.IN_PROCESS);
            if (existingInvite != null) {
                return existingInvite;
            } else {
                Invite invite = new Invite();
                invite.setSender(sender);
                invite.setInvited(invited);
                invite.setStatus(Invite.Status.IN_PROCESS);
                invite.setGamePrototype(gamePrototype);
                invite.setCreationDate(LocalDateTime.now());

                inviteRepository.save(invite);

                inviteSender.send(invite);

                return invite;
            }
        }
    }

    public List<Invite> getCurrentInvites(User user) {
        return inviteRepository.findAllByInvitedEqualsAndStatusEqualsOrderByCreationDate(
                user, Invite.Status.IN_PROCESS);
    }

    public void declineInvite(long id, User user) throws InviteException {
        Invite invite =  getInviteInProgress(id, user);
        invite.setStatus(Invite.Status.DECLINED);

        inviteRepository.save(invite);
    }

    private Invite getInviteInProgress(long id, User user) throws InviteException {
        Invite invite = inviteRepository.findById(id).orElse(null);
        if (invite == null) {
            throw new InviteException(
                    "Invite with id " + id + " doesn't exists");
        } else if (!invite.getInvited().equals(user)) {
            throw new InviteException(
                    "Wrong user " + user + " for invite with id " + id);
        } else if (invite.getStatus() != Invite.Status.IN_PROCESS) {
            throw new InviteException(
                    "Wrong status [" + invite.getStatus() + "] for invite with id " + id);
        }

        return invite;
    }

    public void deleteInactiveInvites() {
        LOG.info("Deleting all declined and accepted invites from db");
        inviteRepository.deleteAllByStatusIn(
                Set.of(Invite.Status.ACCEPTED, Invite.Status.DECLINED));
    }
}
