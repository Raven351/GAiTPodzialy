package com.ravensu.gaitpodzialy.appdata;

import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class AssignmentCountdownFinder {
    private Assignment assignment;
    private LocalDateTime assignmentStartDateTime;
    private LocalDateTime assignmentEndDateTime;
    private AssignmentStatus assignmentStatus;

    public AssignmentCountdownFinder(@NotNull Assignment assignment) {
        this.assignment = assignment;
        this.assignmentStartDateTime = assignment.AssignmentStartDateTime;
        this.assignmentEndDateTime = assignment.AssignmentEndDateTime;
        this.assignmentStatus = new AssignmentStatusFinder(assignment).getAssignmentStatus();
    }

    public String getTimeLeft(){
        switch (assignmentStatus){
            case WILL_START:
                return getTimeLeftString(LocalDateTime.now(), assignmentStartDateTime);
            case ONGOING:
                return getTimeLeftString(LocalDateTime.now(), assignmentEndDateTime);
            case FINISHED:
                return getTimeLeftString(assignmentEndDateTime, LocalDateTime.now());
            default:
                return "";
        }
    }

    private String getTimeLeftString(LocalDateTime startDateTime, LocalDateTime endDateTime){
        Duration duration = Duration.between(startDateTime, endDateTime);
        Locale localePL = new Locale("pl", "PL");
        String timeLeft = "";
        if (duration.toHours() >= 24){
            timeLeft = String.format(localePL, "%dD %dh %02dm", duration.toDays(), duration.toHours() % 24, duration.toMinutes() % 60);
        }
        else if (duration.toHours() >= 1){
            timeLeft = String.format(localePL, "%dh %02dm", duration.toHours(), duration.toMinutes() % 60);
        }
        else {
            timeLeft = String.format(localePL, "%02dm", duration.toMinutes());
        }
        return timeLeft;
    }
}
