package com.example.gabinet_psychologiczny.Other;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarUtils {
    private static LocalDate selectedDate;

    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                .withLocale(new Locale("pl"));
        return date.format(formatter);
    }

    public static String formattedDateShort(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
                .withLocale(new Locale("pl"));
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withLocale(new Locale("pl"));
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
                .withLocale(new Locale("pl"));
        return date.format(formatter);
    }

    public static String dayMonthFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd.MM")
                .withLocale(new Locale("pl"));
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate startDate = getMonday(selectedDate);
        LocalDate endDate = startDate.plusWeeks(1);

        while (startDate.isBefore(endDate))
        {
            days.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return days;
    }

    public static ArrayList<LocalDate> daysInMonthArray(YearMonth yearMonth)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        while (startDate.isBefore(endDate.plusDays(1)))
        {
            days.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return days;
    }

    private static LocalDate getMonday(LocalDate startDate)
    {
        LocalDate oneWeekAgo = startDate.minusWeeks(1);

        while (startDate.isAfter(oneWeekAgo))
        {
            if(startDate.getDayOfWeek() == DayOfWeek.MONDAY)
                return startDate;

            startDate = startDate.minusDays(1);
        }

        return null;
    }

}
