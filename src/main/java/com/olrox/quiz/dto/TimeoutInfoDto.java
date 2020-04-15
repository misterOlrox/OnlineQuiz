package com.olrox.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeoutInfoDto implements InfoDto {
    private final String type = "timeout.info";
    private boolean isTimeout;
    private long timeLeft;
    private int questionInd;

    @Override
    public String getType() {
        return type;
    }
}
