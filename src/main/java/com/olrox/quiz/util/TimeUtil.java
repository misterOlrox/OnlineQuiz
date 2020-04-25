package com.olrox.quiz.util;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public abstract class TimeUtil {

    public static int getTimeInSecondsFromStringWithMinuteAndSecond(String time) {
        String[] parsedTime = time.split(":");

        int minutes = Integer.parseInt(parsedTime[0]);
        int seconds = Integer.parseInt(parsedTime[1]);

        return minutes * 60 + seconds;
    }

    public static String getStringFrom(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.getDayOfMonth() + " "
                + StringUtils.capitalize(localDateTime.getMonth().toString().toLowerCase()) + " "
                + localDateTime.getYear() + ", "
                + addZeroIfLessThenTen(localDateTime.getHour()) + ":"
                + addZeroIfLessThenTen(localDateTime.getMinute()) + ":"
                + addZeroIfLessThenTen(localDateTime.getSecond());
    }

    public static String addZeroIfLessThenTen(int number) {
        return number < 10 ? "0" + number : String.valueOf(number);
    }

    public static long getMillis(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
    }
}
