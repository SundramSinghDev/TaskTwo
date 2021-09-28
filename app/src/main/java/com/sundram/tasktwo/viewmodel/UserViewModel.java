package com.sundram.tasktwo.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sundram.tasktwo.model.Response;
import com.sundram.tasktwo.model.UserDataModel;
import com.sundram.tasktwo.network.ApiResponse;
import com.sundram.tasktwo.network.UserApiCall;
import com.sundram.tasktwo.repository.UserRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {

    private static final String TAG = "UserViewModel";
    MutableLiveData<List<UserDataModel>> getUserData = null;
    public MutableLiveData<String> errorData = new MutableLiveData<>();
    UserApiCall apiCall;
    UserRepository repository;

    @Inject
    public UserViewModel(UserApiCall apiCall, UserRepository repository) {
        this.apiCall = apiCall;
        this.repository = repository;
    }

    public MutableLiveData<List<UserDataModel>> getUserData(Context context, Integer params) {
        getUserData = new MutableLiveData<>();
        apiCall.getUserData(apiResponse, context, params);
        return getUserData;
    }


    private final ApiResponse apiResponse = new ApiResponse() {
        @Override
        public void OnSuccess(Object response) {
            Response response1 = (Response) response;
            getUserData.postValue(response1.getData());
        }

        @Override
        public void OnError(String error) {
            errorData.postValue(error);
        }
    };

    @Override
    protected void onCleared() {
        super.onCleared();
        errorData=null;
        getUserData=null;
    }
}
