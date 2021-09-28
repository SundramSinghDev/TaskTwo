package com.sundram.tasktwo.apiservices;

import com.google.gson.JsonElement;
import com.sundram.tasktwo.utils.ConstantUtils;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApiService {
//    FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).
    @GET(ConstantUtils.BASE_URL)
    Observable<JsonElement> getUserData(@Query("page") Integer params);
}
