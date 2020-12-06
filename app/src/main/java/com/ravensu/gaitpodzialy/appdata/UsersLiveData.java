package com.ravensu.gaitpodzialy.appdata;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UsersLiveData {
    private static boolean usersDataLoaded = false;
    private static final MutableLiveData<User> mainUser = new MutableLiveData<>();
    private static final MutableLiveData<User> currentlySelectedUser = new MutableLiveData<>();
    private static final MutableLiveData<ConcurrentHashMap<String, User>> users = new MutableLiveData<>(new ConcurrentHashMap<String, User>());

    public static boolean getUsersDataLoaded(){
        return usersDataLoaded;
    }

    public static void setUsersDataLoaded(boolean state){
        usersDataLoaded = state;
    }

    public static LiveData<User> getMainUserLiveData(){
        return mainUser;
    }

    public static void setMainUser(User user){
        mainUser.setValue(user);
    }

    public static LiveData<User> getCurrentlySelectedUserLiveData(){
        return currentlySelectedUser;
    }

    public static void setCurrentlySelectedUser(User user){
        currentlySelectedUser.setValue(user);
    }

    public static void postCurrentlySelectedUser(User user){
        currentlySelectedUser.postValue(user);
    }

    public static LiveData<ConcurrentHashMap<String, User>> getUsersLiveData(){
        return users;
    }

    public static synchronized void addUserData(User user){
        ConcurrentHashMap<String, User> usersMap = users.getValue();
        if (usersMap == null) {
            usersMap = new ConcurrentHashMap<>();
        }
        usersMap.put(user.UserId, user);
        users.setValue(usersMap);
    }

    public static synchronized void addUserDataAsync(User user){
        ConcurrentHashMap<String, User> usersMap = users.getValue();
        if (usersMap == null) {
            usersMap = new ConcurrentHashMap<>();
        }
        usersMap.put(user.UserId, user);
        users.postValue(usersMap);
    }

    public static void removeUserData(String userId){
        ConcurrentHashMap<String, User> users = getUsersLiveData().getValue();
        if (users != null) {
            users.remove(userId);
        }
        UsersLiveData.users.setValue(users);
    }

    public static boolean loadUsersData(Context context) {
        if (SavedAppLogins.existsAny(context)){
            List<Future<User>> usersFutureList = new ArrayList<>();
            loadDataToList(context, usersFutureList);
            addUsersToUsersLiveData(usersFutureList);
            setUpAppMainUser(context);
        }
        return currentlySelectedUser.getValue() != null;
    }

    public static boolean loadUsersDataAsync(Context context){
        if (SavedAppLogins.existsAny(context)){
            List<Future<User>> usersFutureList = new ArrayList<>();
            loadDataToList(context, usersFutureList);
            addUsersToUsersLiveDataAsync(usersFutureList);
            setUpAppMainUserAsync(context);
        }
        return currentlySelectedUser.getValue() != null;
    }

    private static void setUpAppMainUserAsync(Context context) {
        mainUser.postValue(users.getValue().get(SavedAppMainLogin.GetMainLoginUserId(context)));
        if (currentlySelectedUser.getValue() == null){
            currentlySelectedUser.postValue(mainUser.getValue());
        }
    }

    private static void addUsersToUsersLiveDataAsync(List<Future<User>> usersFutureList) {
        for (Future<User> userFuture : usersFutureList){
            try{
                UsersLiveData.addUserDataAsync(userFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setUpAppMainUser(Context context) {
        mainUser.setValue(users.getValue().get(SavedAppMainLogin.GetMainLoginUserId(context)));
        if (currentlySelectedUser.getValue() == null){
            currentlySelectedUser.setValue(mainUser.getValue());
        }
    }

    private static void loadDataToList(Context context, List<Future<User>> usersFutureList) {
        final Map<String, ?> savedUsersCredentials = SavedAppLogins.getAllCredentials(context);
        ExecutorService executorService = Executors.newFixedThreadPool(savedUsersCredentials.size());
        for (Map.Entry<String, ?> entry : savedUsersCredentials.entrySet()){
            ScrapUserDataTask scrapUserDataTask = new ScrapUserDataTask(entry.getKey(), entry.getValue().toString());
            Future<User> userFuture = executorService.submit(scrapUserDataTask);
            usersFutureList.add(userFuture);
        }
    }

    private static void addUsersToUsersLiveData(List<Future<User>> usersFutureList) {
        for (Future<User> userFuture : usersFutureList){
            try{
                UsersLiveData.addUserData(userFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
