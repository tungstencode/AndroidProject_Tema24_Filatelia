package com.partenie.alex.filatelie.util;

import androidx.room.TypeConverter;

import java.util.Date;

public class DataConverter {

    @TypeConverter
    public static Date fromTimestamp(Long timestamp) {
        return timestamp != null ? new Date(timestamp)
                : null;
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date != null ? date.getTime()
                : null;
    }
}