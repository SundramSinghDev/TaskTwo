package com.sundram.tasktwo.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sundram.tasktwo.model.Response;
import com.sundram.tasktwo.repository.UserRepository;
import com.sundram.tasktwo.utils.CommonLoader;
import com.sundram.tasktwo.utils.ConnectionUtils;
import com.sundram.tasktwo.utils.ConstantUtils;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserApiCall {

    UserRepository userRepository;
    private static final String TAG = "UserApiCall";
    Context context;

    @Inject
    CommonLoader commonLoader;

    @Inject
    ConnectionUtils connectionUtils;

    @Inject
    public UserApiCall(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void init(Context context) {
        this.context = context;
        commonLoader.createDialog(context);
        connectionUtils.init(context);
    }

    public void getUserData(ApiResponse apiResponse, Context context, Integer params) {
        if (connectionUtils.checkConnectivity()) {
            commonLoader.showDialog();
            userRepository.getUserData(params)
                    .subscribeOn(Schedulers.io())
                    .map((Function<JsonElement, Object>) jsonElement -> {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        Response response = null;
                        response = new GsonBuilder().create().fromJson(jsonObject.toString(), Response.class);

                        return response;
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        commonLoader.hideDialog();
                        Log.i(TAG, "REPONSE: " + result);
                        apiResponse.OnSuccess(result);
                    }, error -> {
                        commonLoader.hideDialog();
                        apiResponse.OnError(ConstantUtils.OPS);
                        Log.i(TAG, "ERROR: " + error);
                    });
        } else {
            commonLoader.hideDialog();
            apiResponse.OnError(ConstantUtils.CONNECTION_ERROR);
            Toast.makeText(context, ConstantUtils.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
        }

    }

}
