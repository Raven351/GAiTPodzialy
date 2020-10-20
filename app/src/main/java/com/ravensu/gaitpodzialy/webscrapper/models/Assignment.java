package com.ravensu.gaitpodzialy.webscrapper.models;

import androidx.annotation.Nullable;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

public class Assignment {
    public LocalDate Date;
    public String DriverNumber;
    public String DriverName;
    public String AssignmentCode;
    public String AssignmentStartLocation;
    public String AssignmentEndLocation;
    public LocalTime AssignmentStartTime;
    public LocalTime AssignmentEndTime;
    public LocalTime AssignmentDuration;
    public String Comments;

    public boolean isSameData(Assignment assignment){
        return this.DriverNumber.equals(assignment.DriverNumber) &&
                this.Date.equals(assignment.Date) &&
                this.AssignmentCode.equals(assignment.AssignmentCode) &&
                this.AssignmentStartLocation.equals(assignment.AssignmentStartLocation) &&
                this.AssignmentEndLocation.equals(assignment.AssignmentEndLocation) &&
                this.AssignmentStartTime.equals(assignment.AssignmentStartTime) &&
                this.AssignmentEndTime.equals(assignment.AssignmentEndTime) &&
                this.AssignmentDuration.equals(assignment.AssignmentDuration) &&
                this.Comments.equals(assignment.Comments);
    }
}
