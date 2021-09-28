package com.sundram.tasktwo.network;

public interface ApiResponse {
    void OnSuccess(Object response);

    void OnError(String error);
}
