package com.sundram.tasktwo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

public class SharedPrefUtils {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    int PRIVATE_MODE=0;
    Context context;
    private static final String PREF_NAME  = "GENDER_FILTER";
    private static final String SELECTED_GENDER = "SELECTED_GENDER";

    @Inject
    public SharedPrefUtils(){}

    public void init(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void saveSelectedGender(String gender){
        editor.putString(SELECTED_GENDER,gender);
        editor.commit();
    }
    public String getSelectedGender(){
        return preferences.getString(SELECTED_GENDER,"null");
    }
}
