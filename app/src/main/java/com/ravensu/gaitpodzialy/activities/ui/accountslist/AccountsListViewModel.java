package com.ravensu.gaitpodzialy.activities.ui.accountslist;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.data.SavedAppLogins;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AccountsListViewModel extends ViewModel {
    private MutableLiveData<ConcurrentHashMap<String, User>> users;
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
