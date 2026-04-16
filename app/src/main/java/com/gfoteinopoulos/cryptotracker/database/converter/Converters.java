package com.gfoteinopoulos.cryptotracker.database.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class Converters {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<Double> fromString(String value) {
        if (value == null) return null;
        Type listType = new TypeToken<List<Double>>() {}.getType();
        return  gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Double> list) {
        if (list == null) return null;
        return  gson.toJson(list);
    }
}
