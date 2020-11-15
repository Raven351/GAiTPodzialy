package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import java.util.ArrayList;

public class AssignmentsListViewModel extends AndroidViewModel {
    private final LiveData<ArrayList<Assignment>> assignments;

    public AssignmentsListViewModel(@NonNull Application application) {
        super(application);
        assignments = Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> user.Assignments);
    }

    public LiveData<ArrayList<Assignment>> getAssignments(){
        return assignments;
    }
}
