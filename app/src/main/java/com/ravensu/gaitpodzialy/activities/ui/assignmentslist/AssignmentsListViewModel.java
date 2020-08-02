package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import java.util.ArrayList;

public class AssignmentsListViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Assignment>> assignments;

    public AssignmentsListViewModel(@NonNull Application application) {
        super(application);
        assignments = new MutableLiveData<>(UsersData.getCurrentlySelectedUser().Assignments);
    }

    public LiveData<ArrayList<Assignment>> getAssignments(){
        if (assignments == null){
            assignments = new MutableLiveData<ArrayList<Assignment>>();
            loadAssignments();
        }
        return assignments;
    }

    private void loadAssignments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                assignments = new MutableLiveData<>(UsersData.getCurrentlySelectedUser().Assignments);
            }
        }).start();
    }
}
