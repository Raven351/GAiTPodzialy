package com.ravensu.gaitpodzialy.activities.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.data.UsersData;

public class MainViewPagerViewModel extends ViewModel {
    private String TAG = "MainViewPagerViewModel";
    private MutableLiveData<String> driverId;
    private MutableLiveData<String> driverName;

    public LiveData<String> getDriverId(){
        if (driverId == null){
            driverId = new MutableLiveData<>();
            loadDriverId();
        }
        return driverId;
    }

    public LiveData<String> getDriverName(){
        if (driverName == null){
            driverName = new MutableLiveData<>();
            loadDriverName();
        }
        return driverName;
    }

    private void loadDriverName() {
        if (UsersData.getCurrentlySelectedUser().Assignments.size() > 0){
            driverName.setValue(UsersData.getCurrentlySelectedUser().Assignments.get(0).DriverName);
        }
        else driverName.setValue("");
    }

    private void loadDriverId() {
        driverId.setValue(UsersData.getCurrentlySelectedUserId());
    }
}
