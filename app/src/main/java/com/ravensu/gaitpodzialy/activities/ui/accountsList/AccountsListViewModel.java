package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import android.app.Application;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.concurrent.ConcurrentHashMap;

public class AccountsListViewModel extends AndroidViewModel {
    private LiveData<ConcurrentHashMap<String, User>> usersLiveData = UsersLiveData.getUsersLiveData();
    private final MutableLiveData<ConcurrentHashMap<String, User>> users = new MutableLiveData<>();
    private final MediatorLiveData<ConcurrentHashMap<String, User>> usersMerger = new MediatorLiveData<>();
    private LiveData<User> mainUserLiveData = UsersLiveData.getMainUserLiveData();
    private final MutableLiveData<User> mainUser = new MutableLiveData<>();
    private final MediatorLiveData<User> mainUserMerger = new MediatorLiveData<>();
    private LiveData<User> currentlySelectedUserLiveData = UsersLiveData.getMainUserLiveData();
    private final MutableLiveData<User> currentlySelectedUser = new MutableLiveData<>();
    private final MediatorLiveData<User> currentlySelectedUserMerger = new MediatorLiveData<>();


    public AccountsListViewModel(@NonNull Application application) {
        super(application);
        usersMerger.addSource(usersLiveData, usersMerger::setValue);
        usersMerger.addSource(users, usersMerger::setValue);
        mainUserMerger.addSource(mainUserLiveData, mainUserMerger::setValue);
        mainUserMerger.addSource(mainUser, mainUserMerger::setValue);
        currentlySelectedUserMerger.addSource(currentlySelectedUser, currentlySelectedUserMerger::setValue);
        currentlySelectedUserMerger.addSource(currentlySelectedUserLiveData, currentlySelectedUserMerger::setValue);
    }

    public LiveData<ConcurrentHashMap<String, User>> getUsersLiveData(){
        loadUsers();
        return usersMerger;
    }

    public LiveData<User> getMainUserLiveData(){
        loadMainUser();
        return mainUserMerger;
    }

    public LiveData<User> getCurrentlySelectedUserLiveData(){
        loadCurrentlySelectedUser();
        return currentlySelectedUserMerger;
    }

    private void loadCurrentlySelectedUser() {
        currentlySelectedUserLiveData = UsersLiveData.getCurrentlySelectedUserLiveData();
    }

    public void setCurrentlySelectedUser(User user){
        this.currentlySelectedUser.setValue(user);
        UsersLiveData.setCurrentlySelectedUser(user);
    }

    private void loadMainUser() {
        mainUserLiveData = UsersLiveData.getMainUserLiveData();
    }

    public void setMainUser(User user){
        this.mainUser.setValue(user);
        UsersLiveData.setMainUser(user);
    }

    public void addUser(User user){
        ConcurrentHashMap<String, User> users = getUsersLiveData().getValue();
        users.put(user.UserId, user);
        this.users.setValue(users);
    }

    public void removeUser(User user){
        ConcurrentHashMap<String, User> users = getUsersLiveData().getValue();
        users.remove(user.UserId);
        this.users.setValue(users);
    }

    private void loadUsers(){
        usersLiveData = Transformations.map(UsersLiveData.getUsersLiveData(), users -> users);
    }



}
