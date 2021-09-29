package com.sundram.tasktwo.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sundram.tasktwo.model.UserRoomDBModel;

@Dao
public interface UserRoomDBDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserDataIntoRoomDB(UserRoomDBModel response);

    @Query("SELECT * FROM USERDATA WHERE id=:id")
    LiveData<UserRoomDBModel> getAllUserDataFromRoomDB(Integer id);
}
