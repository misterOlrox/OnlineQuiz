package com.olrox.quiz.rest;

import com.olrox.quiz.dto.ErrorDto;
import com.olrox.quiz.dto.InviteDto;
import com.olrox.quiz.entity.Invite;
import com.olrox.quiz.entity.User;
import com.olrox.quiz.service.InviteService;
import com.olrox.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class InviteRestController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;

    @PostMapping("/api/invite")
    public ResponseEntity<?> invite(@AuthenticationPrincipal User sender,
                                    @RequestParam Long prototypeId,
                                    @RequestParam Long invitedId) {
        var optionalInvited = userService.findUserById(invitedId);
        if (optionalInvited.isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorDto("Invited user doesn't exists"), HttpStatus.NOT_FOUND);
        }

        Invite invite = inviteService.addInvite(sender, optionalInvited.get(), prototypeId);

        return new ResponseEntity<>(InviteDto.from(invite), HttpStatus.OK);
    }

    @GetMapping("/api/invites")
    public ResponseEntity<?> getInvites(@AuthenticationPrincipal User user) {
        var invites = inviteService.getCurrentInvites(user);

        return new ResponseEntity<>(
                invites.stream().map(InviteDto::from).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}
