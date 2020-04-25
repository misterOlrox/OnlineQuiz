package com.olrox.quiz.controller.util;

import com.olrox.quiz.entity.Role;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerUtilsTest {

    @Test
    void extractRoles() {
        Map<String, String> map = Map.of(
                "123", "123",
                Role.ADMIN.name(), "on",
                Role.USER.name(), "on",
                "moderator", "on"
        );

        Set<Role> extractedRoles = ControllerUtils.extractRoles(map);

        assertEquals(2, extractedRoles.size());
        assertTrue(extractedRoles.containsAll(Set.of(Role.ADMIN, Role.USER)));
    }
}