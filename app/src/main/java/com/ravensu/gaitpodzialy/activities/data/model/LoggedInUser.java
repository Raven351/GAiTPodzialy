package com.ravensu.gaitpodzialy.activities.data.model;

import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import java.util.ArrayList;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private ArrayList<Assignment> assignmentList;

    public LoggedInUser(String userId, String displayName, ArrayList<Assignment> assignmentList) {
        this.userId = userId;
        this.displayName = displayName;
        this.assignmentList = assignmentList;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
