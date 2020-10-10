package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AssignmentsFirstSecondViewModel extends ViewModel {
    private String TAG = "AssignmentsTodayTomorrowViewModel";
    private MutableLiveData<Assignment> firstAssignment;
    private MutableLiveData<Assignment> secondAssignment;
    private MutableLiveData<Integer> isOngoing = new MutableLiveData<>(R.string.status_willstart);
    private Integer firstAssignmentIndex = 0;

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

    public LiveData<Integer> getIsOnGoing(){
        return isOngoing;
    }
    //todo fix case when user has no assignments
    private void loadFirstAssignment(){
        LocalDate date = LocalDate.now();
        ArrayList<Assignment> assignments = getSortedAssignments();
        int i = 0;
        if (assignments.size() == 0 ) firstAssignment.setValue(new Assignment());
        while (i<assignments.size()){
            Log.d(TAG, "loadFirstAssignment: " + assignments.get(i).Date + assignments.get(i).AssignmentStartTime);
            LocalDate assignmentDate = assignments.get(i).Date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Log.d(TAG, "loadFirstAssignment: " + date.toString() + "      " + assignmentDate.toString());
            if (assignmentDate.isEqual(date)) Log.d(TAG, "loadFirstAssignment: IS EQUAL");
            if (assignmentDate.isEqual(date) && LocalTime.MIDNIGHT.equals(assignments.get(i).AssignmentStartTime) && LocalTime.MIDNIGHT.equals(assignments.get(i).AssignmentEndTime)){
                firstAssignment.setValue(assignments.get(i));
                firstAssignmentIndex = i;
                isOngoing.setValue(R.string.status_dayoff);
                break;
            }
            else if (assignmentDate.isEqual(date) && LocalTime.now().isBefore(assignments.get(i).AssignmentEndTime)){
                firstAssignment.setValue(assignments.get(i));
                firstAssignmentIndex = i;
                if (LocalTime.now().isAfter(assignments.get(i).AssignmentStartTime)){
                    isOngoing.setValue(R.string.status_ongoing);
                }
                break;
            }
            else if (assignmentDate.isAfter(date)){
                firstAssignment.setValue(assignments.get(i));
                firstAssignmentIndex = i;
                isOngoing.setValue(R.string.status_willstart);
                break;
            }
            else i++;
        }
    }

    private void loadSecondAssignment(){
        ArrayList<Assignment> assignments = getSortedAssignments();
        if (firstAssignment.getValue() != null){
            for (int i = firstAssignmentIndex + 1; i<assignments.size(); i++){
                if(i >= assignments.size()){
                    break;
                }
                else {
                    secondAssignment.setValue(assignments.get(i));
                    return;
                }
            }
        }
        secondAssignment.setValue(new Assignment());
        //if (assignments.indexOf(firstAssignment.getValue()) + 1 < assignments.size()) secondAssignment.setValue(assignments.get(assignments.indexOf(firstAssignment) + 1));
    }

    private ArrayList<Assignment> getAssignmentsByDate(LocalDate date) {
        ArrayList<Assignment> assignments = UsersData.getCurrentlySelectedUser().Assignments;
        ArrayList<Assignment> assignmentsByDate = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++) {
            LocalDate assignmentDate = assignments.get(i).Date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (assignmentDate.equals(date)) assignmentsByDate.add(assignments.get(i));
        }
        Collections.sort(assignmentsByDate, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment o1, Assignment o2) {
                return o1.AssignmentStartTime.compareTo(o2.AssignmentStartTime);
            }
        });
        Log.d(TAG, "getAssignmentsByDate: Sorted assignments: ");
        for (Assignment assignment : assignmentsByDate) {
            Log.d(TAG, assignment.AssignmentStartTime.toString());
        }
        return assignmentsByDate;
    }

    private ArrayList<Assignment> getSortedAssignments(){
        ArrayList<Assignment> assignments = UsersData.getCurrentlySelectedUser().Assignments;
        Collections.sort(assignments, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment o1, Assignment o2) {
                return o1.Date.compareTo(o2.Date);
            }
        }.thenComparing(new Comparator<Assignment>() {
            @Override
            public int compare(Assignment o1, Assignment o2) {
                return o1.AssignmentStartTime.compareTo(o2.AssignmentStartTime);
            }
        }));
        Log.d(TAG, "getSortedAssignments: Sorted assignments");
        for (Assignment assignment : assignments){
            Log.d(TAG, assignment.Date.toString() + assignment.AssignmentStartTime.toString());
        }
        return assignments;
    }

    private boolean isAssignmentDayOff(Assignment assignment){
        return assignment.AssignmentStartTime.equals(LocalTime.MIDNIGHT) &&
                assignment.AssignmentEndTime.equals(LocalTime.MIDNIGHT) &&
                assignment.AssignmentStartLocation.contains("-");
    }
}
