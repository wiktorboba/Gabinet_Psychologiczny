package com.example.gabinet_psychologiczny.Database.Converters;

import androidx.room.TypeConverter;

import java.time.LocalTime;

public class TimeConverter {

    @TypeConverter
    public static LocalTime fromTimestamp(String value) {
        return value == null ? null : LocalTime.parse(value);
    }

    @TypeConverter
    public static String dateToTimestamp(LocalTime date) {
        return date == null ? null : date.toString();
    }
}
