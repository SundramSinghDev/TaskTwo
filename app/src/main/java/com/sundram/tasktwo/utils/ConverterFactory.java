package com.sundram.tasktwo.utils;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sundram.tasktwo.model.Response;

import java.lang.reflect.Type;

public class ConverterFactory {

    @TypeConverter
    public static Response fromString(String value){
        Type type =new TypeToken<Response>() {}.getType();
        return  new Gson().fromJson(value,type);
    }

    @TypeConverter
    public static String fromArrayList(Response response){
        Gson gson = new Gson();
        String json = gson.toJson(response);
        return json;
    }
}
