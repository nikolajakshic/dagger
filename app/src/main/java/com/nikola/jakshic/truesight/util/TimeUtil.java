package com.nikola.jakshic.truesight.util;


public class TimeUtil {

    private static final long SECOND_IN_MILLIS = 1000;
    private static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    private static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    private static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    private static final long MONTH_IN_MILLIS = DAY_IN_MILLIS * 30;
    private static final long YEAR_IN_MILLIS = MONTH_IN_MILLIS * 12;

    public static long secondsBetweenPeriods(long start, long end) {
        long time = end - start;

        return time / DAY_IN_MILLIS;
    }

    public static long minutesBetweenPeriods(long start, long end) {
        long time = end - start;

        return time / MINUTE_IN_MILLIS;
    }

    public static long hoursBetweenPeriods(long start, long end) {
        long time = end - start;

        return time / HOUR_IN_MILLIS;
    }

    public static long daysBetweenPeriods(long start, long end) {
        long time = end - start;

        return time / DAY_IN_MILLIS;
    }

    public static long monthsBetweenPeriods(long start, long end) {
        long time = end - start;

        return time / MONTH_IN_MILLIS;
    }

    public static long yearsBetweenPeriods(long start, long end) {
        long time = end - start;

        return time / YEAR_IN_MILLIS;
    }
}
