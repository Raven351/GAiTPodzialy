package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AssignmentsTodayTomorrowViewModel extends ViewModel {
    private String TAG = "AssignmentsTodayTomorrowViewModel";
    private MutableLiveData<Assignment> firstAssignment;
    private MutableLiveData<Assignment> secondAssignment;
    private MutableLiveData<Boolean> isOngoing = new MutableLiveData<>(false);

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

    public LiveData<Boolean> getIsOnGoing(){
        return isOngoing;
    }

    private void loadFirstAssignment(){
        LocalDate date = LocalDate.now();
        LocalDate loopEndDate = date.plusDays(30);
        ArrayList<Assignment> assignments = getSortedAssignments();
        int i = 0;
        while (i<assignments.size()){
            Log.d(TAG, "loadFirstAssignment: " + assignments.get(i).Date + assignments.get(i).AssignmentStartTime);
            LocalDate assignmentDate = assignments.get(i).Date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Log.d(TAG, "loadFirstAssignment: " + date.toString() + "      " + assignmentDate.toString());
            if (assignmentDate.isEqual(date)) Log.d(TAG, "loadFirstAssignment: IS EQUAL");
            if (assignmentDate.isEqual(date) && LocalTime.now().isBefore(assignments.get(i).AssignmentEndTime)){
                firstAssignment.setValue(assignments.get(i));
                if (LocalTime.now().isAfter(assignments.get(i).AssignmentStartTime)){
                    isOngoing.setValue(true);
                }
                break;
            }
            else if (assignmentDate.isAfter(date)){
                firstAssignment.setValue(assignments.get(i));
                isOngoing.setValue(false);
                break;
            }
            else i++;
        }
    }

    private void loadSecondAssignment(){
        ArrayList<Assignment> assignments = getSortedAssignments();
        if (assignments.indexOf(firstAssignment.getValue()) + 1 < assignments.size()) secondAssignment.setValue(assignments.get(assignments.indexOf(firstAssignment) + 1));
        else secondAssignment.setValue(new Assignment());
    }

    private void loadAssignments(){
        LocalDate date = LocalDate.now();
        LocalDate loopEndDate = LocalDate.now().plusDays(30);
        ArrayList<Assignment> assignments = getAssignmentsByDate(date);
        boolean isFirstAssignmentSet = false;
        boolean isSecondAssignmentSet = false;
        while ((!isFirstAssignmentSet && !isSecondAssignmentSet ) || date.getDayOfYear() == loopEndDate.getDayOfYear()){
            if (!assignments.isEmpty()){
                for (int i = 0; i< assignments.size(); i++){
                    if (LocalTime.now().isBefore(assignments.get(i).AssignmentEndTime)){
                        if (isFirstAssignmentSet) {
                            secondAssignment.setValue(assignments.get(i));
                            isSecondAssignmentSet = true;
                        }
                        else {
                            firstAssignment.setValue(assignments.get(i));
                            isFirstAssignmentSet = true;
                            if (LocalTime.now().isAfter(assignments.get(i).AssignmentStartTime)){
                                isOngoing.setValue(true);
                            }
                            if (i+1 != assignments.size()) {
                                secondAssignment.setValue(assignments.get(i+1));
                                isSecondAssignmentSet = true;
                            }
                        }
                    }
                }
            }
            Log.d(TAG, "loadAssignments: Loop:" + date.toString());
            date = date.plusDays(1);
        }
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 5000);

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
}
