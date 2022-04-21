package com.groupsix.project3_cst438.roomDB;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class CustomTypeConverters {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Integer> stringToList(String stringId) {
        if(stringId == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> storiesIdList = gson.fromJson(stringId, listType);
        return storiesIdList;
    }

    @TypeConverter
    public static String listToString(List<Integer> list) {
        return gson.toJson(list);
    }

}
