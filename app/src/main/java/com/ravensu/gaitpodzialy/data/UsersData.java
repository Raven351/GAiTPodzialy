package com.ravensu.gaitpodzialy.data;

import android.content.Context;
import android.util.Log;

import com.ravensu.gaitpodzialy.webscrapper.GAiTWebScrapper;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Accesses and stores users' data and info about app's users such as currently selected user and main user.
 */
public class UsersData {
    private static User mainUser;
    private static User currentlySelectedUser; //user currently selected in app
    private final static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    /**
     * Adds User object to users ConcurrentHashMap
     * @param user User object to add
     */
    private static synchronized void addUserData(User user){
        users.put(user.UserId, user);
    }

    /**
     * Returns User object with given UserId
     * @param userId UserId of user object to return
     * @return User object
     */
    public static User getUserByUserId(String userId){
        return users.get(userId);
    }

    /**
     * Returns ArrayList with Assignment objects for given UserId
     * @param userId UserId for which Assignments ArrayList will be returned
     * @return ArrayList with Assignment objects
     */
    public static ArrayList<Assignment> getUsersAssignments(String userId){
        return Objects.requireNonNull(users.get(userId)).Assignments;
    }

    /**
     * Returns User object of currently selected user
     * @return User object
     */
    public static User getCurrentlySelectedUser(){
        return currentlySelectedUser;
    }

    /**
     * Returns UserId of currently selected user
     * @return String containing UserId
     */
    public static String getCurrentlySelectedUserId(){
        return currentlySelectedUser.UserId;
    }

    public static User getMainUser(){
        return mainUser;
    }

    public static ConcurrentHashMap<String, User> getAllUsers(){return users;}

    public static void setCurrentlySelectedUser(String userId){
        currentlySelectedUser = getUserByUserId(userId);
    }

    public static void setMainUser(String userId){
        mainUser = getUserByUserId(userId);
    }

    public static void removeUser(String userId){
        users.remove(userId);
    }

    public static void loadUsersData(Context context) throws InterruptedException {
        if (SavedAppLogins.ExistsAny(context)){
            final Map<String, ?> savedUsersCredentials = SavedAppLogins.GetAllCredentials(context);
            Thread loadUsersDataThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Map.Entry<String, ?> entry : savedUsersCredentials.entrySet()){
                        GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper(entry.getKey(), entry.getValue().toString());
                        ArrayList<Assignment> assignments = gAiTWebScrapper.ScrapAssignmentsTable(gAiTWebScrapper.GetGAiTWebsite());
                        Log.d("AppUsersData", "loadUsersData: Saving user data: " + entry.getKey()  + " - Assignments count: "+ assignments.size());
                        User user = new User(entry.getKey(), entry.getValue().toString(), assignments);
                        UsersData.addUserData(user);
                        users.put(entry.getKey(), new User(entry.getKey(), entry.getValue().toString(), assignments));
                    }
                }
            });
            loadUsersDataThread.start();
            loadUsersDataThread.join(5000);
            mainUser = users.get(SavedAppMainLogin.GetMainLoginUserId(context));
            currentlySelectedUser = mainUser;
            Log.d("AppUsersData", "loadUsersData: Main user: " + getUsersAssignments(currentlySelectedUser.UserId).size());
        }
    }
}
