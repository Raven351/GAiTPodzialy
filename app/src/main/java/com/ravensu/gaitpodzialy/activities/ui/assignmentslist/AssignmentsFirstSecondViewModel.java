package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.data.AssignmentFinder;
import com.ravensu.gaitpodzialy.data.AssignmentStatus;
import com.ravensu.gaitpodzialy.data.AssignmentStatusFinder;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class AssignmentsFirstSecondViewModel extends ViewModel {
    private final String TAG = "AssignmentsFirstSecond";
    private MutableLiveData<Assignment> firstAssignment;
    private MutableLiveData<Assignment> secondAssignment;
    private final MutableLiveData<Integer> firstAssignmentStatus = new MutableLiveData<>(R.string.status_willstart);
    private final AssignmentFinder assignmentFinder = new AssignmentFinder(UsersData.getCurrentlySelectedUser().Assignments);

    public LiveData<Assignment> getFirstAssignment(){
        if (firstAssignment == null){
            firstAssignment = new MutableLiveData<>();
            loadFirstAssignment();
        }
        return firstAssignment;
    }

    public LiveData<Assignment> getSecondAssignment(){
        if (secondAssignment == null){
            secondAssignment = new MutableLiveData<>();
            loadSecondAssignment();
        }
        return secondAssignment;
    }

    public LiveData<Integer> getFirstAssignmentStatus(){
        return firstAssignmentStatus;
    }

    private void loadFirstAssignment() {
        firstAssignment.setValue(assignmentFinder.getFirstUpcomingAssignment());
        if (firstAssignment.getValue() != null) {
            AssignmentStatus assignmentStatus = new AssignmentStatusFinder(firstAssignment.getValue()).getAssignmentStatus();
            switch (assignmentStatus) {
                case WILL_START:
                    firstAssignmentStatus.setValue(R.string.status_willstart);
                    break;
                case ONGOING:
                    firstAssignmentStatus.setValue(R.string.status_ongoing);
                    break;
                case DAY_OFF:
                    firstAssignmentStatus.setValue(R.string.status_dayoff);
                    break;
                default:
                    firstAssignmentStatus.setValue(R.string.no_data);
            }
        } else {
            firstAssignmentStatus.setValue(R.string.no_data);
        }
    }

    private void loadSecondAssignment(){
        if (firstAssignment.getValue() != null){
            secondAssignment.setValue(assignmentFinder.getUpcomingAssignmentBySequence(2));
        }
        else secondAssignment.setValue(new Assignment());
        //if (assignments.indexOf(firstAssignment.getValue()) + 1 < assignments.size()) secondAssignment.setValue(assignments.get(assignments.indexOf(firstAssignment) + 1));
    }
}
