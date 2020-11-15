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

public class UsersLiveDataSingleton {
    private static volatile UsersLiveDataSingleton usersLiveDataSingletonInstance;
    private MutableLiveData<User> mainUser;
    private MutableLiveData<User> currentlySelectedUser;
    private MutableLiveData<ConcurrentHashMap<String, User>> users;

    private UsersLiveDataSingleton(){
        if (usersLiveDataSingletonInstance != null){
            throw new RuntimeException("Singleton class. Use getInstance() method to get the single instance of this class");
        }
    }

    public static UsersLiveDataSingleton getInstance(){
        if (usersLiveDataSingletonInstance == null){
            synchronized (UsersLiveDataSingleton.class){
                if (usersLiveDataSingletonInstance == null) usersLiveDataSingletonInstance = new UsersLiveDataSingleton();
                usersLiveDataSingletonInstance.mainUser = new MutableLiveData<>();
                usersLiveDataSingletonInstance.currentlySelectedUser = new MutableLiveData<>();
                usersLiveDataSingletonInstance.users = new MutableLiveData<>();
                usersLiveDataSingletonInstance.users.setValue(new ConcurrentHashMap<String, User>());
            }
        }
        return usersLiveDataSingletonInstance;
    }

    public LiveData<User> getMainUser(Context context){
        if (mainUser == null || mainUser.getValue() == null){
            mainUser = new MutableLiveData<>();
            loadUsersData(context);
        }
        return mainUser;
    }

    public LiveData<User> getCurrentlySelectedUser(Context context){
        if (currentlySelectedUser == null || currentlySelectedUser.getValue() == null){
            currentlySelectedUser = new MutableLiveData<>();
            loadUsersData(context);
        }
        return currentlySelectedUser;
    }

    public LiveData<ConcurrentHashMap<String, User>> getUsers(Context context){
        if (users == null || users.getValue() == null){
            users = new MutableLiveData<>();
            users.setValue(new ConcurrentHashMap<String, User>());
            loadUsersData(context);
        }
        return users;
    }

    public synchronized void addUserData(User user){
        ConcurrentHashMap<String, User> users = this.users.getValue();
        if (users != null){
            users.put(user.UserId, user);
            this.users.setValue(users);
        }
    }

    public boolean loadUsersData(Context context){
        if (SavedAppLogins.existsAny(context)) {
            final Map<String, ?> savedUsersCredentials = SavedAppLogins.getAllCredentials(context);
            ExecutorService executorService = Executors.newFixedThreadPool(savedUsersCredentials.size());
            List<Future<User>> userFutureList = new ArrayList<Future<User>>();
            for (Map.Entry<String, ?> entry : savedUsersCredentials.entrySet()){
                ScrapUserDataTask scrapUserDataTask = new ScrapUserDataTask(entry.getKey(), entry.getValue().toString());
                Future<User> userFuture = executorService.submit(scrapUserDataTask);
                userFutureList.add(userFuture);
            }
            for (Future<User> userFuture : userFutureList){
                try {
                    addUserData(userFuture.get());
                }
                catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
            }
        }
        if (users.getValue() != null){
            mainUser.setValue(users.getValue().get(SavedAppMainLogin.GetMainLoginUserId(context)));
            currentlySelectedUser.setValue(mainUser.getValue());
        }
        return currentlySelectedUser.getValue() != null;
    }

}
