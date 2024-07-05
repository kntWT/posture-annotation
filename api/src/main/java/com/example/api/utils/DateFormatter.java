package com.example.api.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss.SSS");

    DateFormatter() {
    }

    public static String format(OffsetDateTime dateTime) {
        return dateTime.format(formatter);
    }

    public static OffsetDateTime parse(String dateTime) {
        return OffsetDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String format(OffsetDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static OffsetDateTime parse(String dateTime, String pattern) {
        return OffsetDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(OffsetDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

}
