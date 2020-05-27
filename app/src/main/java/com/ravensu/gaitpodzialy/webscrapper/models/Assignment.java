package com.ravensu.gaitpodzialy.webscrapper.models;

import java.time.LocalTime;
import java.util.Date;

public class Assignment implements Cloneable {
    public Date Date;
    public String DriverNumber;
    public String DriverName;
    public String AssignmentCode;
    public String AssignmentStartLocation;
    public String AssignmentEndLocation;
    public LocalTime AssignmentStartTime;
    public LocalTime AssignmentEndTime;
    public LocalTime AssignmentDuration;
    public String Comments;
}
