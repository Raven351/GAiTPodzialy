package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.appdata.AssignmentFinder;
import com.ravensu.gaitpodzialy.appdata.AssignmentStatus;
import com.ravensu.gaitpodzialy.appdata.AssignmentStatusFinder;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

public class AssignmentsFirstSecondViewModel extends ViewModel {
    private final String TAG = "AssignmentsFirstSecond";
    private LiveData<Assignment> firstAssignment;
    private LiveData<Assignment> secondAssignment;
    private LiveData<Integer> firstAssignmentStatus;


    public LiveData<Assignment> getFirstAssignment(){
        loadFirstAssignment();
        return firstAssignment;
    }

    public LiveData<Assignment> getSecondAssignment(){
        loadSecondAssignment();
        return secondAssignment;
    }

    public LiveData<Integer> getFirstAssignmentStatus(){
        loadFirstAssignmentStatus();
        return firstAssignmentStatus;
    }

    private void loadFirstAssignment(){
        firstAssignment = Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> {
            AssignmentFinder assignmentFinder = new AssignmentFinder(user.Assignments);
            return assignmentFinder.getFirstUpcomingAssignment();
        });
    }

    private void loadFirstAssignmentStatus(){
        firstAssignmentStatus = Transformations.map(getFirstAssignment(), firstAssignment -> {
            if (firstAssignment != null){
                AssignmentStatus assignmentStatus = new AssignmentStatusFinder(firstAssignment).getAssignmentStatus();
                    switch (assignmentStatus) {
                        case WILL_START:
                            return R.string.status_willstart;
                        case ONGOING:
                            return R.string.status_ongoing;
                        case DAY_OFF:
                            return R.string.status_dayoff;
                        default:
                            return R.string.no_data;
                    }
            }
            else return R.string.no_data;
        });
    }

    private void loadSecondAssignment(){
        secondAssignment = Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> {
            AssignmentFinder assignmentFinder = new AssignmentFinder(user.Assignments);
            return assignmentFinder.getUpcomingAssignmentBySequence(2);
        });
    }
}
