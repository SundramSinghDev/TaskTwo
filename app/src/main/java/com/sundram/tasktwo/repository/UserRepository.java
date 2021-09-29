package com.sundram.tasktwo.repository;

import androidx.lifecycle.LiveData;

import com.google.gson.JsonElement;
import com.sundram.tasktwo.apiservices.UserApiService;
import com.sundram.tasktwo.model.UserRoomDBModel;
import com.sundram.tasktwo.roomdb.UserRoomDBDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class UserRepository {

    private UserApiService userApiService;
    private UserRoomDBDao userRoomDBDao;
    ExecutorService executor = Executors.newSingleThreadExecutor();
//    Handler handler = new Handler(Looper.getMainLooper());

    @Inject
    public UserRepository(UserApiService userApiService, UserRoomDBDao userRoomDBDao) {
        this.userApiService = userApiService;
        this.userRoomDBDao = userRoomDBDao;
    }


    public Observable<JsonElement> getUserData(Integer params) {
        return userApiService.getUserData(params);
    }

    public void insertUserDataIntoRoomDB(UserRoomDBModel roomDBModel) {
        executor.execute(() -> {
            userRoomDBDao.insertUserDataIntoRoomDB(roomDBModel);
//            handler.post(() -> {
//                //UI Thread work here
//            });
        });
    }

    public LiveData<UserRoomDBModel> getUserDataFromRoomDB(Integer id) {
        return userRoomDBDao.getAllUserDataFromRoomDB(id);
    }
}
