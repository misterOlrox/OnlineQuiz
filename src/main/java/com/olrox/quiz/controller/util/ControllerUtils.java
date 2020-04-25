package com.olrox.quiz.controller.util;

import com.olrox.quiz.entity.Role;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class ControllerUtils {

    private static Set<String> existingRoles = Arrays.stream(Role.values())
            .map(Role::name)
            .collect(Collectors.toUnmodifiableSet());

    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

    public static Set<Role> extractRoles(Map<String, String> params) {
        Set<String> intersection = new HashSet<>(params.keySet());
        intersection.retainAll(existingRoles);

        return intersection.stream().map(Role::valueOf).collect(Collectors.toSet());
    }
}
