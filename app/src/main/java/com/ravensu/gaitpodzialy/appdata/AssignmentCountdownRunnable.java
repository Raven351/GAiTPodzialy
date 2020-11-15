package com.ravensu.gaitpodzialy.appdata;

import android.os.Handler;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import java.util.Locale;

public class AssignmentCountdownRunnable implements Runnable {
    private final Assignment assignment;
    private final TextView textView;
    private final LocalDateTime assignmentStartDateTime;
    private final LocalDateTime assignmentEndDateTime;
    private final AssignmentStatus assignmentStatus;
    Handler timerHandler = new Handler();

    public AssignmentCountdownRunnable(Assignment assignment, TextView textView) {
        this.assignment = assignment;
        this.textView = textView;
        this.assignmentStartDateTime = assignment.AssignmentStartDateTime;
        this.assignmentEndDateTime = assignment.AssignmentEndDateTime;
        this.assignmentStatus = new AssignmentStatusFinder(assignment).getAssignmentStatus();
    }

    private String getTimeLeft(){
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

    @Override
    public void run(){
        textView.setText(getTimeLeft());
        timerHandler.postDelayed(this, 15000);
    }
}
