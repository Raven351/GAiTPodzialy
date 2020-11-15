package com.ravensu.gaitpodzialy.appdata;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Accesses "app_main_login" SharedPreferences that store users credentials (userId and password)
 */
public class SavedAppMainLogin {
    private SharedPreferences sharedPreferences;
    private SavedAppMainLogin(){

    }

    /**
     * Accesses "app_main_login" SharedPreferences
     * @param context Current app context
     * @return SharedPreferences containing users credentials
     */
    private static SharedPreferences getPreferences(Context context){
        String PREF_NAME = "app_main_login";
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Gets UserId of current main user from "app_main_login" SharedPreferences
     * @param context Current app context
     * @return String with UserId of current main user
     */
    public static String GetMainLoginUserId(Context context){
        return getPreferences(context).getString("userId", null);
    }

    /**
     * Sets and saves given UserId to "app_main_login" SharedPreferences
     * @param context Current app context
     * @param userId UserId to set as new main app's user
     */
    public static void SetMainLoginUserId(Context context, String userId){
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("userId", userId);
        preferencesEditor.apply();
    }

    /**
     * Gets user name of current main user from "app_main_login" SharedPreferences if any exists
     * @param context Current app context
     * @return String containing user's name in form "FirstName LastName". Returns null if none exists.
     */
    public static String GetMainLoginUserName(Context context){
        return getPreferences(context).getString("userName", null);
    }

    /**
     * Sets and saves given user's name to "app_main_login" SharedPreferences
     * @param context Current app context
     * @param userName UserName to set as new main app's user's name
     */
    public static void SetMainLoginUserName(Context context, String userName){
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("userName", userName);
        preferencesEditor.apply();
    }
}
