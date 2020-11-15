package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.concurrent.ConcurrentHashMap;

public class AccountsListViewModel extends AndroidViewModel {
    private final LiveData<ConcurrentHashMap<String, User>> users = UsersLiveData.getUsersLiveData();

    public AccountsListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ConcurrentHashMap<String, User>> getUsers(){
        return users;
    }

}
