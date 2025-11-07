package com.example.community;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidation {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date, DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean dateRangeValid(String startDate, String endDate) {
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
            return false;
        }
        return !LocalDate.parse(startDate, DATE_FORMAT).isAfter(LocalDate.parse(endDate, DATE_FORMAT));
    }

    public static boolean isInDateRange(String date, String startDate, String endDate) {
        if (!isValidDateFormat(date) || !isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
            return false;
        }
        LocalDate d = LocalDate.parse(date, DATE_FORMAT);
        LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
        LocalDate end = LocalDate.parse(endDate, DATE_FORMAT);
        return !d.isBefore(start) && !d.isAfter(end);
    }

    public static String getCurrentDate() {
        return LocalDate.now().format(DATE_FORMAT);
    }
}
