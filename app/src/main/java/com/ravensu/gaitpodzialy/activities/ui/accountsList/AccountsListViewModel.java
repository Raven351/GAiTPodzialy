package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.concurrent.ConcurrentHashMap;

public class AccountsListViewModel extends AndroidViewModel {
    private MutableLiveData<ConcurrentHashMap<String, User>> users;

    public AccountsListViewModel(@NonNull Application application) {
        super(application);
        users = new MutableLiveData<>(UsersData.getAllUsers());
    }

    public LiveData<ConcurrentHashMap<String, User>> getUsers(){
        if (users == null){
            users = new MutableLiveData<ConcurrentHashMap<String, User>>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                users = new MutableLiveData<>(UsersData.getAllUsers());
            }
        }).start();
    }

}
