package com.ravensu.gaitpodzialy.appdata;

import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

public class AssignmentStatusFinder {
    private final Assignment assignment;
    private final LocalDateTime assignmentStartDateTime;
    private final LocalDate assignmentStartDate;
    private final LocalTime assignmentStartTime;
    private final LocalDateTime assignmentEndDateTime;
    private final LocalDate assignmentEndDate;
    private final LocalTime assignmentEndTime;

    public AssignmentStatusFinder(Assignment assignment) {
        this.assignment = assignment;
        this.assignmentStartDateTime = assignment.AssignmentStartDateTime;
        this.assignmentStartDate = assignment.AssignmentStartDateTime.toLocalDate();
        this.assignmentStartTime = assignment.AssignmentStartDateTime.toLocalTime();
        this.assignmentEndDateTime = assignment.AssignmentEndDateTime;
        this.assignmentEndDate = assignment.AssignmentEndDateTime.toLocalDate();
        this.assignmentEndTime = assignment.AssignmentEndDateTime.toLocalTime();
    }

    public AssignmentStatus getAssignmentStatus(){
        if (isAssignmentFreeDay()) return AssignmentStatus.DAY_OFF;
        else if (assignmentEndDateTime.isBefore(LocalDateTime.now())) return AssignmentStatus.FINISHED;
        else if (assignmentStartDateTime.isAfter(LocalDateTime.now())) return AssignmentStatus.WILL_START;
        else if (assignmentStartDateTime.isBefore(LocalDateTime.now()) && assignmentEndDateTime.isAfter(LocalDateTime.now())) return AssignmentStatus.ONGOING;
        else return AssignmentStatus.UNSPECIFIED;
    }

    private boolean isAssignmentFreeDay(){
        return assignmentStartTime == LocalTime.MIDNIGHT && assignmentEndTime == LocalTime.MIDNIGHT;
    }

}
