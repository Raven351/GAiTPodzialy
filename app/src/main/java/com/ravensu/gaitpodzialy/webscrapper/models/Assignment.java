package com.ravensu.gaitpodzialy.webscrapper.models;

import androidx.annotation.Nullable;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

public class Assignment {
    public LocalDateTime AssignmentStartDateTime;
    public LocalDateTime AssignmentEndDateTime;
    public String DriverNumber;
    public String DriverName;
    public String AssignmentCode;
    public String AssignmentStartLocation;
    public String AssignmentEndLocation;
    public LocalTime AssignmentDuration;
    public String Comments;

    public boolean isSameData(Assignment assignment){
        return this.DriverNumber.equals(assignment.DriverNumber) &&
                this.AssignmentCode.equals(assignment.AssignmentCode) &&
                this.AssignmentStartLocation.equals(assignment.AssignmentStartLocation) &&
                this.AssignmentEndLocation.equals(assignment.AssignmentEndLocation) &&
                this.AssignmentStartDateTime.equals(assignment.AssignmentStartDateTime) &&
                this.AssignmentEndDateTime.equals(assignment.AssignmentEndDateTime) &&
                this.AssignmentDuration.equals(assignment.AssignmentDuration) &&
                this.Comments.equals(assignment.Comments);
    }

    public void setAssignmentEndDateTime(LocalDateTime assignmentEndDateTime) {
        AssignmentEndDateTime = assignmentEndDateTime;
    }

    public void setAssignmentEndDateTime(LocalTime assignmentEndTime){
        LocalDate assignmentEndDate = this.AssignmentStartDateTime.toLocalDate();
        if (assignmentEndTime.isBefore(AssignmentStartDateTime.toLocalTime())){
            assignmentEndDate = assignmentEndDate.plusDays(1);
        }
        this.AssignmentEndDateTime = LocalDateTime.of(assignmentEndDate, assignmentEndTime);
    }
}
