package me.jacob.discordbot.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
    private TimeUtil() {
    }

    public static String dateToString(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
    }

    public static int toDays(OffsetDateTime offsetDateTime) {
        return (int) ChronoUnit.DAYS.between(offsetDateTime, OffsetDateTime.now());
    }

}
