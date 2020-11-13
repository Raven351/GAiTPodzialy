package com.ravensu.gaitpodzialy.data;

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

    public static void removeUserData(String userId){
        ConcurrentHashMap<String, User> users = getUsersLiveData().getValue();
        users.remove(userId);
        UsersLiveData.users.setValue(users);
    }
}
