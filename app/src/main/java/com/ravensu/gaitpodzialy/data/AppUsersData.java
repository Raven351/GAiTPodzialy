package com.ravensu.gaitpodzialy.data;

import android.content.Context;
import android.util.Log;

import com.ravensu.gaitpodzialy.webscrapper.GAiTWebScrapper;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppUsersData {
    private static User mainUser;
    private static User currentlySelectedUser; //user currently selected in app
    private final static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static synchronized void addUserData(User user){
        users.put(user.UserId, user);
    }

    public static User getUserByUserId(String userId){
        return users.get(userId);
    }

    public static ArrayList<Assignment> getUsersAssignments(String userId){
        return users.get(userId).Assignments;
    }

    public static User getCurrentlySelectedUser(){
        return currentlySelectedUser;
    }

    public static String getCurrentlySelectedUserId(){
        return currentlySelectedUser.UserId;
    }

    public static ConcurrentHashMap<String, User> getAllUsers(){return users;}

    public static void loadUsersData(Context context) throws InterruptedException {
        if (AppLogins.ExistsAny(context)){
            final Map<String, ?> savedUsersCredentials = AppLogins.GetAllCredentials(context);
            Thread loadUsersDataThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Map.Entry<String, ?> entry : savedUsersCredentials.entrySet()){
                        GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper(entry.getKey(), entry.getValue().toString());
                        ArrayList<Assignment> assignments = gAiTWebScrapper.ScrapAssignmentsTable(gAiTWebScrapper.GetGAiTWebsite());
                        Log.d("AppUsersData", "loadUsersData: Saving user data: " + entry.getKey()  + " - Assignments count: "+ assignments.size());
                        User user = new User(entry.getKey(), entry.getValue().toString(), assignments);
                        AppUsersData.addUserData(user);
                        users.put(entry.getKey(), new User(entry.getKey(), entry.getValue().toString(), assignments));
                    }
                }
            });
            loadUsersDataThread.start();
            loadUsersDataThread.join(5000);
            mainUser = users.get(AppMainLogin.GetMainLoginUserId(context));
            currentlySelectedUser = mainUser;
            Log.d("AppUsersData", "loadUsersData: Main user: " + getUsersAssignments(currentlySelectedUser.UserId).size());
        }
    }
}
