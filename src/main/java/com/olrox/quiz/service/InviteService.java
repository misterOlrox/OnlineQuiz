package com.olrox.quiz.service;

import com.olrox.quiz.entity.GamePrototype;
import com.olrox.quiz.entity.Invite;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.repository.GamePrototypeRepository;
import com.olrox.quiz.repository.InviteRepository;
import com.olrox.quiz.service.sender.InviteSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            Invite existingInvite = inviteRepository.findFirstByGamePrototypeAndSenderAndInvited(
                    gamePrototype, sender, invited);
            if (existingInvite != null) {
                return existingInvite;
            } else {
                Invite invite = new Invite();
                invite.setSender(sender);
                invite.setInvited(invited);
                invite.setStatus(Invite.Status.IN_PROCESS);
                invite.setGamePrototype(gamePrototypeOptional.get());

                inviteRepository.save(invite);

                inviteSender.send(invite);

                return invite;
            }
        }
    }

    public List<Invite> getCurrentInvites(User user) {
        return inviteRepository.findAllByInvitedEqualsAndStatusEquals(user, Invite.Status.IN_PROCESS);
    }
}
