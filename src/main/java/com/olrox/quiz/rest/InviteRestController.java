package com.olrox.quiz.rest;

import com.olrox.quiz.dto.InviteRequestDto;
import com.olrox.quiz.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InviteRestController {

    @PostMapping("/api/invite")
    public ResponseEntity<?> invite(@AuthenticationPrincipal User sender,
                                    @RequestBody InviteRequestDto inviteRequestDto) {

        return null;
    }
}
