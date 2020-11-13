package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.data.UsersLiveData;
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
