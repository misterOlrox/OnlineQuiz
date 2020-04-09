package com.olrox.quiz.util;

public abstract class TimeUtil {

    public static int getTimeInSecondsFromStringWithMinuteAndSecond(String time) {
        String[] parsedTime = time.split(":");

        int minutes = Integer.parseInt(parsedTime[0]);
        int seconds = Integer.parseInt(parsedTime[1]);

        return minutes * 60 + seconds;
    }
}
