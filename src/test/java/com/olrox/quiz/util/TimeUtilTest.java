package com.olrox.quiz.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilTest {

    @Test
    void testGetTimeInSecondsFromStringWithMinuteAndSecond_1() {
        String oneMinute = "1:00";

        Integer timeInSeconds = TimeUtil.getTimeInSecondsFromStringWithMinuteAndSecond(oneMinute);

        assertEquals(60, timeInSeconds);
    }

    @Test
    void testGetTimeInSecondsFromStringWithMinuteAndSecond_2() {
        String oneMinute = "0:01";

        Integer timeInSeconds = TimeUtil.getTimeInSecondsFromStringWithMinuteAndSecond(oneMinute);

        assertEquals(1, timeInSeconds);
    }

    @Test
    void testGetTimeInSecondsFromStringWithMinuteAndSecond_3() {
        String oneMinute = "22:44";

        Integer timeInSeconds = TimeUtil.getTimeInSecondsFromStringWithMinuteAndSecond(oneMinute);

        assertEquals(22 * 60 + 44, timeInSeconds);
    }
}