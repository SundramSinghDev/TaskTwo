package com.sundram.tasktwo.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sundram.tasktwo.apiservices.UserApiService;
import com.sundram.tasktwo.model.Response;
import com.sundram.tasktwo.repository.UserRepository;
import com.sundram.tasktwo.utils.ConstantUtils;

import org.json.JSONObject;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserApiCall {

    UserRepository userRepository;
    private static final String TAG = "UserApiCall";

    @Inject
    public UserApiCall(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void getUserData(ApiResponse apiResponse, Context context, Integer params) {
        userRepository.getUserData(params)
                .subscribeOn(Schedulers.io())
                .map(new Function<JsonElement, Object>() {
                    @Override
                    public Object apply(JsonElement jsonElement) throws Throwable {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        Response response = null;
                            Log.i(TAG, "REPONSE JOBJ: " + jsonObject.toString());
                            response = new GsonBuilder().create().fromJson(jsonObject.toString(), Response.class);

                        return response;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.i(TAG, "REPONSE: " + result);
                    apiResponse.OnSuccess(result);
                }, error -> {
                    apiResponse.OnError(ConstantUtils.OPS);
                    Log.i(TAG, "ERROR: " + error);
                });

    }

}
