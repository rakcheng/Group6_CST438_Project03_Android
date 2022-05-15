package com.groupsix.project3_cst438.roomDB;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.groupsix.project3_cst438.roomDB.entities.Stories;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
        return gson.fromJson(stringId, listType);
    }

    @TypeConverter
    public static String storiesListToString(List<Integer> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static String stringFromStories(List<Stories> storiesList) {
        // Serialize stories into json string
        Gson gson = new Gson();
        return gson.toJson(storiesList);
    }

    @TypeConverter
    public static List<Stories> JsonStoriesListToStories(String jsonStr) {
        // Deserialize json string to stories object
        Type listType = new TypeToken<List<Stories>>() {}.getType();
        return new Gson().fromJson(jsonStr, listType);
    }


}
