package com.olrox.quiz.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorDto {
    private final String errorMessage;
}
