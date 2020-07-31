package com.ravensu.gaitpodzialy.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Accesses "app_logins" SharedPreferences that store users credentials (userId and password)
 */
public class SavedAppLogins {
    private SharedPreferences sharedPreferences;
    private SavedAppLogins(){

    }

    /**
     * Accesses "app_logins" SharedPreferences
     * @param context Current app context
     * @return SharedPreferences containing users credentials
     */
    private static SharedPreferences getPreferences(Context context){
        String PREF_NAME = "app_logins";
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Gets all credentials of users stored in "app_logins" SharedPreferences
     * @param context Current app context
     * @return Map containing credentials of all users saved in SharedPreferences. Key is UserId, value is password for given UserId.
     */
    public static Map<String, ?> GetAllCredentials(Context context){
        return getPreferences(context).getAll();
    }

    /**
     * Checks if given userId is saved in "app_logins" SharedPreferences
     * @param context Current app context
     * @param userId UserId to search for in SharedPreferences
     * @return True if exists. False if not.
     */
    public static boolean Exists(Context context, String userId){
        return getPreferences(context).contains(userId);
    }

    /**
     * Checks if there is at least 1 user saved in "app_logins" SharedPreferences
     * @param context Current app context
     * @return false if there 0 users saved in app_logins context, or true if else
     */
    public static boolean ExistsAny(Context context){
        if (getPreferences(context).getAll().size() == 0 ) return false;
        else return true;
    }

    /**
     * Adds user credidentials to "app_logins" in "app_logins" SharedPreferences.
     * @param context Current app context
     * @param userId UserId to add.
     * @param password Password of given UserId to add.
     * @throws IllegalArgumentException if user was already added.
     */
    public static void SaveCredentials(Context context, String userId, String password){
        if(Exists(context, userId)) throw new IllegalArgumentException("UserId already saved");
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(userId, password);
        preferencesEditor.apply();
    }

    /**
     * Removes from "app_logins" SharedPreferences credentials of given UserId
     * @param context Current app context
     * @param userId UserId of which credentials shall be removed.
     */
    public static void RemoveCredentials(Context context, String userId){
        if(!Exists(context,userId)) throw new NullPointerException("UserId not found");
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.remove(userId);
        preferencesEditor.apply();
    }
}
