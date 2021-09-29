package com.sundram.tasktwo.di;

import android.app.Application;

import androidx.room.Room;

import com.sundram.tasktwo.roomdb.UserRoomDB;
import com.sundram.tasktwo.roomdb.UserRoomDBDao;
import com.sundram.tasktwo.utils.ConstantUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class UserRoomDBModule {

    @Provides
    @Singleton
    static UserRoomDB provideRoomDBClient(Application application){
        return Room.databaseBuilder(application,
                UserRoomDB.class,
                ConstantUtils.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    static UserRoomDBDao provideUserRoomDBApiService(UserRoomDB userRoomDB){
        return userRoomDB.UserLocal();
    }

}
