package com.ravensu.gaitpodzialy.data;

import android.content.Context;
import android.util.Log;

import com.ravensu.gaitpodzialy.webscrapper.GAiTWebScrapper;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;
import com.ravensu.gaitpodzialy.webscrapper.models.Document;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Accesses and stores users' data and info about app's users such as currently selected user and main user.
 */
public class UsersData {
    private static User mainUser;
    private static User currentlySelectedUser; //user currently selected in app
    private final static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private static boolean isUsersDataAccessAvailable = true;

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

    public static boolean getIsUsersDataAccessAvailable() {return isUsersDataAccessAvailable;}

    public static void setIsUsersDataAccessAvailable(boolean isUsersDataAccessAvailable) { UsersData.isUsersDataAccessAvailable = isUsersDataAccessAvailable;}

    public static void loadUsersData(Context context) throws InterruptedException {
        if (SavedAppLogins.ExistsAny(context)){
            final Map<String, ?> savedUsersCredentials = SavedAppLogins.GetAllCredentials(context);
            ExecutorService executorService = Executors.newFixedThreadPool(savedUsersCredentials.size());
            List<Future<User>> userFutureList = new ArrayList<Future<User>>();
            for (Map.Entry<String, ?> entry : savedUsersCredentials.entrySet()){
                ScrapUserDataTask scrapUserDataTask = new ScrapUserDataTask(entry.getKey(), entry.getValue().toString());
                Future<User> userFuture = executorService.submit(scrapUserDataTask);
                userFutureList.add(userFuture);
            }
            for (Future<User> userFuture : userFutureList){
                try {
                    UsersData.addUserData(userFuture.get());
                }
                catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
            }
            mainUser = users.get(SavedAppMainLogin.GetMainLoginUserId(context));
            currentlySelectedUser = mainUser;
        }
    }
}
