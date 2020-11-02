package com.ravensu.gaitpodzialy.data;

import android.util.Log;

import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AssignmentFinder {
    private final ArrayList<Assignment> assignments;
    private final Assignment firstUpcomingAssignment;
    private final int firstUpcomingAssignmentIndex;

    public AssignmentFinder(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
        this.firstUpcomingAssignment = findFirstUpcomingAssignment();
        this.firstUpcomingAssignmentIndex = findFirstUpcomingAssignmentIndex();
    }

    public ArrayList<Assignment> getSortedByDateAndTime(){
        assignments.sort(new Comparator<Assignment>() {
            @Override
            public int compare(Assignment o1, Assignment o2) {
                return o1.AssignmentStartDateTime.compareTo(o2.AssignmentStartDateTime);
            }
        });
        return assignments;
    }

    public Assignment getFirstUpcomingAssignment() {
        return firstUpcomingAssignment;
    }

    private Assignment findFirstUpcomingAssignment(){
        ArrayList<Assignment> assignments = getSortedByDateAndTime();
        LocalDateTime currentDateTime = LocalDateTime.now();
        int i = 0;
        if (assignments.size() == 0) return new Assignment();
        while (i < assignments.size()){
            LocalDateTime assignmentEndDateTime = assignments.get(i).AssignmentEndDateTime;
            if (assignmentEndDateTime.isBefore(currentDateTime)) i++;
            else return assignments.get(i);
        }
        return new Assignment();
    }

    private int findFirstUpcomingAssignmentIndex(){
        if (assignments.size() > 0) return assignments.indexOf(firstUpcomingAssignment);
        else return 0;
    }

    public Assignment getUpcomingAssignmentBySequence(int assignmentIndex){
        assignmentIndex = firstUpcomingAssignmentIndex + assignmentIndex - 1;
        if (assignmentIndex >= firstUpcomingAssignmentIndex && assignmentIndex < assignments.size()){
            return assignments.get(assignmentIndex);
        }
        else return new Assignment();
    }
}
