package com.sundram.tasktwo.repository;

import com.google.gson.JsonElement;
import com.sundram.tasktwo.apiservices.UserApiService;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class UserRepository {
    private UserApiService userApiService;

    @Inject
    public UserRepository(UserApiService userApiService) {
        this.userApiService = userApiService;
    }
    public Observable<JsonElement> getUserData(Integer params){
        return userApiService.getUserData(params);
    }
}
