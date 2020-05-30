package com.ravensu.gaitpodzialy.activities.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import java.util.ArrayList;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Parcelable {

    private String userId;
    private String displayName;
    private ArrayList<Assignment> assignmentsList;

    public LoggedInUser(String userId, String displayName, ArrayList<Assignment> assignmentsList) {
        this.userId = userId;
        this.displayName = displayName;
        this.assignmentsList = assignmentsList;
    }

    protected LoggedInUser(Parcel in) {
        userId = in.readString();
        displayName = in.readString();
    }

    public static final Creator<LoggedInUser> CREATOR = new Creator<LoggedInUser>() {
        @Override
        public LoggedInUser createFromParcel(Parcel in) {
            return new LoggedInUser(in);
        }

        @Override
        public LoggedInUser[] newArray(int size) {
            return new LoggedInUser[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(displayName);
        dest.writeList(assignmentsList);
    }
}
