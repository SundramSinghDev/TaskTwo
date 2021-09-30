package com.sundram.tasktwo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

public class ConnectionUtils {

    private Context context;

    @Inject
    public ConnectionUtils(){}

    public void init(Context context){
        this.context=context;
    }

    public boolean checkConnectivity() {
        boolean flag = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            flag= networkInfo != null && networkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
