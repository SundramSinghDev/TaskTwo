package com.sundram.tasktwo.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.sundram.tasktwo.model.UserRoomDBModel;
import com.sundram.tasktwo.utils.ConverterFactory;

@Database(entities = {UserRoomDBModel.class}, version = 1, exportSchema = false)
@TypeConverters({ConverterFactory.class})
public abstract class UserRoomDB extends RoomDatabase {

    public abstract UserRoomDBDao UserLocal();

}
