package com.ravensu.gaitpodzialy.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class AppLogins {
    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "app_logins";

    private AppLogins(){

    }

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static Map<String, ?> GetAllCredidentials(Context context){
        return getPreferences(context).getAll();
    }

    public static boolean Exists(Context context, String userId){
        return getPreferences(context).contains(userId);
    }

    /**
     *
     * @param context
     * @return false if there 0 users saved in app_logins context, or true if else
     */
    public static boolean ExistsAny(Context context){
        if (getPreferences(context).getAll().size() == 0 ) return false;
        else return true;
    }

    /**
     * Adds user credidentials to "app_logins" in SharedPreferences.
     * @param context
     * @param userId
     * @param password
     * @throws IllegalArgumentException if user was already added.
     */
    public static void SaveCredidentials(Context context, String userId, String password){
        if(Exists(context, userId)) throw new IllegalArgumentException("UserId already saved");
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(userId, password);
        preferencesEditor.apply();
    }

    public static void RemoveCredidentials(Context context, String userId){
        if(!Exists(context,userId)) throw new NullPointerException("UserId not found");
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.remove(userId);
        preferencesEditor.apply();
    }
}
