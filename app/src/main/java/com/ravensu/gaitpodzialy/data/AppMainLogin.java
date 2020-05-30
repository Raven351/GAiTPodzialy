package com.ravensu.gaitpodzialy.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppMainLogin {
    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "app_main_login";

    private AppMainLogin(){

    }

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String GetMainLoginUserId(Context context){
        return getPreferences(context).getString("userId", null);
    }

    public static void SetMainLoginUserId(Context context, String userId){
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("userId", userId);
        preferencesEditor.apply();
    }

    public static String GetMainLoginUserName(Context context){
        return getPreferences(context).getString("userName", null);
    }

    public static void SetMainLoginUserName(Context context, String userName){
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("userName", userName);
        preferencesEditor.apply();
        Log.d("AppMainLogin", "SetMainLoginUserName: saved username " + GetMainLoginUserName(context) + "to app_main_login shared preferences");
    }
}
