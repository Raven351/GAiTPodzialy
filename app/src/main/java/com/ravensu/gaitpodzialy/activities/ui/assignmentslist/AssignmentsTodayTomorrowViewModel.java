package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AssignmentsTodayTomorrowViewModel extends ViewModel {
    private MutableLiveData<Assignment> firstAssignment = new MutableLiveData<>();
    private MutableLiveData<Assignment> secondAssignment = new MutableLiveData<>();
    private MutableLiveData<Boolean> isOngoing = new MutableLiveData<>(false);

    public LiveData<Assignment> getFirstAssignment(){
        if (firstAssignment == null){
            firstAssignment = new MutableLiveData<Assignment>();
            setAssignments();
        }
        return firstAssignment;
    }

    public LiveData<Assignment> getSecondAssignment(){
        if (secondAssignment == null){
            secondAssignment = new MutableLiveData<Assignment>();
            setAssignments();
        }
        return secondAssignment;
    }

    public LiveData<Boolean> getIsOnGoing(){
        return isOngoing;
    }

    private void setAssignments(){
        LocalDate date = LocalDate.now();
        ArrayList<Assignment> assignments = getAssignmentsByDate(date);
        while (firstAssignment.getValue() == null && secondAssignment.getValue() == null|| date == LocalDate.now().plusDays(30)){
            if (!assignments.isEmpty()){
                for (int i = 0; i< assignments.size(); i++){
                    if (LocalTime.now().isBefore(assignments.get(i).AssignmentEndTime)){
                        if (firstAssignment.getValue() != null) secondAssignment.setValue(assignments.get(i));
                        else {
                            firstAssignment.setValue(assignments.get(i));
                            if (LocalTime.now().isAfter(assignments.get(i).AssignmentStartTime)){
                                isOngoing.setValue(true);
                            }
                            if (i+1 != assignments.size()) secondAssignment.setValue(assignments.get(i+1));
                        }
                    }
                }
            }
        date.plusDays(1);
        }
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
        Log.d("AssignmentsTodayTomorrowViewModel", "getAssignmentsByDate: Sorted assignments: ");
        for (Assignment assignment : assignmentsByDate) {
            Log.d("AssignmentsTodayTomorrowViewModel", assignment.AssignmentStartTime.toString());
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
        return assignments;
    }
}
