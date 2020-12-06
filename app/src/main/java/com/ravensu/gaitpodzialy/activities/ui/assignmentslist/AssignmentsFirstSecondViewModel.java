package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.appdata.AssignmentCountdownFinder;
import com.ravensu.gaitpodzialy.appdata.AssignmentFinder;
import com.ravensu.gaitpodzialy.appdata.AssignmentStatus;
import com.ravensu.gaitpodzialy.appdata.AssignmentStatusFinder;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

public class AssignmentsFirstSecondViewModel extends ViewModel {
    private final String TAG = "AssignmentsFirstSecond";
    private LiveData<Assignment> firstAssignmentLiveData;
    private final MutableLiveData<Assignment> firstAssignment = new MutableLiveData<>();
    private final MediatorLiveData<Assignment> firstAssignmentMerger = new MediatorLiveData<>();
    private LiveData<Assignment> secondAssignmentLiveData;
    private final MutableLiveData<Assignment> secondAssignment = new MutableLiveData<>();
    private final MediatorLiveData<Assignment> secondAssignmentMerger = new MediatorLiveData<>();
    private LiveData<Integer> firstAssignmentStatus;
    private LiveData<String> firstAssignmentTimeLeft;
    private LiveData<Boolean> isProperlyLoggedIn;

    public AssignmentsFirstSecondViewModel() {
        loadFirstAssignment();
        loadSecondAssignment();
        firstAssignmentMerger.addSource(firstAssignmentLiveData, firstAssignmentMerger::setValue);
        firstAssignmentMerger.addSource(firstAssignment, firstAssignmentMerger::setValue);
        secondAssignmentMerger.addSource(secondAssignmentLiveData, secondAssignmentMerger::setValue);
        secondAssignmentMerger.addSource(secondAssignment, secondAssignmentMerger::setValue);
    }


    public LiveData<Assignment> getFirstAssignmentLiveData(){
        loadFirstAssignment();
        return firstAssignmentMerger;
    }

    public void setFirstAssignment(Assignment firstAssignment) {
        this.firstAssignment.setValue(firstAssignment);
    }

    public LiveData<Assignment> getSecondAssignmentLiveData(){
        loadSecondAssignment();
        return secondAssignmentMerger;
    }

    public void setSecondAssignment(Assignment secondAssignment){
        this.secondAssignment.setValue(secondAssignment);
    }

    public LiveData<Integer> getFirstAssignmentStatus(){
        loadFirstAssignmentStatus();
        return firstAssignmentStatus;
    }

    public LiveData<String> getFirstAssignmentTimeLeft(){
        if (firstAssignmentTimeLeft == null){
            firstAssignmentTimeLeft = new MutableLiveData<>();
        }
        loadFirstAssignmentTimeLeft();
        return firstAssignmentTimeLeft;
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

    private void loadFirstAssignmentTimeLeft() {
        firstAssignmentTimeLeft = Transformations.map(getFirstAssignmentLiveData(), assignment -> {
            if (assignment != null){
                if (assignment.AssignmentStartDateTime != null){
                    return new AssignmentCountdownFinder(assignment).getTimeLeft();
                }
            }
            return "";
        });
    }

    private void loadFirstAssignment(){
        firstAssignmentLiveData = Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> {
            AssignmentFinder assignmentFinder = new AssignmentFinder(user.Assignments);
            return assignmentFinder.getFirstUpcomingAssignment();
        });
    }

    private void loadFirstAssignmentStatus(){
        firstAssignmentStatus = Transformations.map(getFirstAssignmentLiveData(), firstAssignment -> {
            if (firstAssignment != null){
                if (firstAssignment.AssignmentStartDateTime != null){
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
            }
           return R.string.no_data;
        });
    }

    private void loadSecondAssignment(){
        secondAssignmentLiveData = Transformations.map(UsersLiveData.getCurrentlySelectedUserLiveData(), user -> {
            AssignmentFinder assignmentFinder = new AssignmentFinder(user.Assignments);
            return assignmentFinder.getUpcomingAssignmentBySequence(2);
        });
    }
}
