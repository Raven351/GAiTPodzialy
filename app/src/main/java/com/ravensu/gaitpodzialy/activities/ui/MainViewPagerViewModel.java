package com.ravensu.gaitpodzialy.activities.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.data.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

public class MainViewPagerViewModel extends ViewModel {
    private String TAG = "MainViewPagerViewModel";
    private MutableLiveData<String> driverName;

    public LiveData<String> getDriverId(){
        return Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> user.UserId);
    }

    public LiveData<String> getDriverName(){
        return Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> {
            if (user.Assignments.size() > 0){
                return user.Assignments.get(0).DriverName;
            }
            else return "";
        });
    }
}
