package com.ravensu.gaitpodzialy.data;

import android.content.Context;
import android.content.SharedPreferences;

public class AppMainLogin {
    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "app_main_login";

    private AppMainLogin(){

    }

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getMainLoginUserId(Context context){
        return getPreferences(context).getString("userId", null);
    }

    public static void setMainLoginUserId(Context context, String userId){
        SharedPreferences.Editor preferencesEditor = getPreferences(context).edit();
        preferencesEditor.putString("userId", userId);
        preferencesEditor.apply();
    }

    public static String getMainLoginUserName(Context context){
        return getPreferences(context).getString("userName", null);
    }

    public static void setMainLoginUserName(Context context, String userName){
        SharedPreferences.Editor preferencesEditor = getPreferences(context).edit();
        preferencesEditor.putString("userName", userName);
        preferencesEditor.apply();
    }
}
