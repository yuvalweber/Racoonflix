package com.example.netflix.database;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;

public class Converters {
    // Convert List<String> to a comma-separated String
    @TypeConverter
    public static String fromList(List<String> list) {
        return list == null ? null : String.join(",", list);
    }

    // Convert a comma-separated String back to List<String>
    @TypeConverter
    public static List<String> fromString(String value) {
        return value == null || value.isEmpty() ? null : Arrays.asList(value.split(","));
    }
}