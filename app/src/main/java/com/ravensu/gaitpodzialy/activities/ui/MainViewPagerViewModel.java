package com.ravensu.gaitpodzialy.activities.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.appdata.UsersLiveData;

public class MainViewPagerViewModel extends ViewModel {
    private String TAG = "MainViewPagerViewModel";
    private MutableLiveData<String> driverName;
    private LiveData<Boolean> isProperlyLoggedIn;

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

    public LiveData<Boolean> getIsProperlyLoggedIn() {
        if (isProperlyLoggedIn == null){
            isProperlyLoggedIn = new MutableLiveData<>();
        }
        loadIsProperlyLoggedIn();
        return isProperlyLoggedIn;
    }

    private void loadIsProperlyLoggedIn() {
        isProperlyLoggedIn = Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> user.isUserProperlyLoggedIn);
    }

}
