package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.appdata.AssignmentCountdownFinder;
import com.ravensu.gaitpodzialy.appdata.AssignmentFinder;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.threeten.bp.format.DateTimeFormatter;

public class AssignmentsFirstSecondFragment extends Fragment {

    private AssignmentsFirstSecondViewModel assignmentsFirstSecondViewModel;
    private Thread firstAssignmentCountdownThread;
    private final Handler mainHandler = new Handler();
    private AssignmentCountdownFinder assignmentCountdownFinder;

    private static final String TAG = "AssignmentsFirstSecondF";

    public static AssignmentsFirstSecondFragment newInstance() {
        return new AssignmentsFirstSecondFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.assignments_today_tomorrow_fragment, container, false);
        final TextView assignmentFirstStatusTextView = view.findViewById(R.id.assignmentFirstStatus);
        final TextView assignmentFirstDateTextView = view.findViewById(R.id.assignmentFirstDate);
        final TextView assignmentFirstCodeTextView = view.findViewById(R.id.assignmentFirstCode);
        final TextView assignmentFirstNoticesTextView = view.findViewById(R.id.assignmentFirstNotices);
        final TextView assignmentFirstTimeStart = view.findViewById(R.id.assignmentFirstTimeStart);
        final TextView assignmentFirstTimeEnd = view.findViewById(R.id.assignmentFirstTimeEnd);
        final TextView assignmentFirstStartLocation = view.findViewById(R.id.assignmentFirstStartLocation);
        final TextView assignmentFirstEndLocation = view.findViewById(R.id.assignmentFirstEndLocation);
        final TextView assignmentFirstTimeTotal = view.findViewById(R.id.assignmentFirstTimeTotal);
        final TextView assignmentFirstWeekDay = view.findViewById(R.id.assignmentFirstWeekDay);
        final TextView assignmentFirstTimeLeft = view.findViewById(R.id.assignmentFirstTimeLeft);
        final TextView assignmentSecondDateTextView = view.findViewById(R.id.assignmentSecondDate);
        final TextView assignmentSecondCodeTextView = view.findViewById(R.id.assignmentSecondCode);
        final TextView assignmentSecondNoticesTextView = view.findViewById(R.id.assignmentSecondNotices);
        final TextView assignmentSecondTimeStart = view.findViewById(R.id.assignmentSecondTimeStart);
        final TextView assignmentSecondTimeEnd = view.findViewById(R.id.assignmentSecondTimeEnd);
        final TextView assignmentSecondStartLocation = view.findViewById(R.id.assignmentSecondStartLocation);
        final TextView assignmentSecondEndLocation = view.findViewById(R.id.assignmentSecondEndLocation);
        final TextView assignmentSecondTimeTotal = view.findViewById(R.id.assignmentSecondTimeTotal);
        final TextView assignmentSecondWeekDay = view.findViewById(R.id.assignmentSecondWeekDay);
        if (UsersLiveData.getCurrentlySelectedUserLiveData().getValue().Assignments.size() == 0){
            assignmentFirstStatusTextView.setVisibility(View.GONE);
            assignmentFirstStartLocation.setVisibility(View.GONE);
            assignmentFirstTimeStart.setVisibility(View.GONE);
            assignmentFirstEndLocation.setVisibility(View.GONE);
            assignmentFirstTimeEnd.setVisibility(View.GONE);
            assignmentFirstDateTextView.setVisibility(View.GONE);
            assignmentFirstCodeTextView.setVisibility(View.GONE);
            assignmentFirstNoticesTextView.setVisibility(View.GONE);
            assignmentFirstTimeTotal.setVisibility(View.GONE);
            assignmentSecondStartLocation.setVisibility(View.GONE);
            assignmentSecondTimeStart.setVisibility(View.GONE);
            assignmentSecondEndLocation.setVisibility(View.GONE);
            assignmentSecondTimeEnd.setVisibility(View.GONE);
            assignmentSecondDateTextView.setVisibility(View.GONE);
            assignmentSecondCodeTextView.setVisibility(View.GONE);
            assignmentSecondNoticesTextView.setVisibility(View.GONE);
            assignmentSecondTimeTotal.setVisibility(View.GONE);
            assignmentFirstWeekDay.setVisibility(View.GONE);
            assignmentSecondWeekDay.setVisibility(View.GONE);
            assignmentFirstTimeLeft.setVisibility(View.GONE);
        }

        firstAssignmentCountdownThread = new Thread(new updateAssignmentFirstTimeLeftRunnable(assignmentFirstTimeLeft, mainHandler));
        firstAssignmentCountdownThread.start();

        assignmentsFirstSecondViewModel = new ViewModelProvider(this).get(AssignmentsFirstSecondViewModel.class);
        assignmentsFirstSecondViewModel.getFirstAssignment().observe(getViewLifecycleOwner(), new Observer<Assignment>() {
            @Override
            public void onChanged(Assignment assignment) {
                if (assignment != null){
                    if (assignment.AssignmentStartDateTime != null && assignment.AssignmentEndDateTime != null){
                        String dateFormat = assignment.AssignmentStartDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        assignmentFirstDateTextView.setText(dateFormat);
                        String weekday = assignment.AssignmentStartDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("EEEE"));
                        weekday = weekday.substring(0, 1).toUpperCase() + weekday.substring(1);
                        assignmentFirstWeekDay.setText(weekday);
                        assignmentFirstTimeEnd.setText(assignment.AssignmentEndDateTime.toLocalTime().toString());
                        assignmentFirstTimeTotal.setText(assignment.AssignmentDuration.toString());
                        assignmentFirstTimeStart.setText(assignment.AssignmentStartDateTime.toLocalTime().toString());
                    }
                    else{
                        assignmentFirstDateTextView.setText("-");
                        assignmentFirstWeekDay.setText("-");
                        assignmentFirstTimeEnd.setText("-");
                        assignmentFirstTimeTotal.setText("-");
                        assignmentFirstTimeStart.setText("-");
                    }
                    assignmentFirstCodeTextView.setText(assignment.AssignmentCode);
                    assignmentFirstNoticesTextView.setText(assignment.Comments);
                    assignmentFirstStartLocation.setText(assignment.AssignmentStartLocation);
                    assignmentFirstEndLocation.setText(assignment.AssignmentEndLocation);
                }
            }
        });
        assignmentsFirstSecondViewModel.getFirstAssignmentTimeLeft().observe(getViewLifecycleOwner(), assignmentFirstTimeLeft::setText);
        assignmentsFirstSecondViewModel.getFirstAssignmentStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer stringResource) {
                String status = getResources().getString(stringResource);
                assignmentFirstStatusTextView.setText(status);
                if (status.equals(getResources().getString(R.string.status_ongoing))) {
                    assignmentFirstStatusTextView.setTextColor(Color.parseColor("#1895f5"));
                    assignmentFirstTimeLeft.setVisibility(View.VISIBLE);
                }
                else if (status.equals(getResources().getString(R.string.status_willstart))){
                    assignmentFirstStatusTextView.setTextColor(Color.parseColor("#19851b"));
                    assignmentFirstTimeLeft.setVisibility(View.VISIBLE);
                }
                else {
                    assignmentFirstStatusTextView.setTextColor(Color.parseColor("#39748f"));
                    assignmentFirstTimeLeft.setVisibility(View.GONE);
                }
            }
        });
        assignmentsFirstSecondViewModel.getSecondAssignment().observe(getViewLifecycleOwner(), new Observer<Assignment>() {
            @Override
            public void onChanged(Assignment assignment) {
                if (assignment != null){
                    if (assignment.AssignmentStartDateTime != null && assignment.AssignmentEndDateTime != null){
                        String dateFormat = assignment.AssignmentStartDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        assignmentSecondDateTextView.setText(dateFormat);
                        String weekday = assignment.AssignmentStartDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("EEEE"));
                        weekday = weekday.substring(0, 1).toUpperCase() + weekday.substring(1);
                        assignmentSecondWeekDay.setText(weekday);
                        assignmentSecondTimeTotal.setText(assignment.AssignmentDuration.toString());
                        assignmentSecondTimeStart.setText(assignment.AssignmentStartDateTime.toLocalTime().toString());
                        assignmentSecondTimeEnd.setText(assignment.AssignmentEndDateTime.toLocalTime().toString());
                    }
                    else {
                        assignmentSecondDateTextView.setText("-");
                        assignmentSecondWeekDay.setText("-");
                        assignmentSecondTimeTotal.setText("-");
                        assignmentSecondTimeStart.setText("-");
                        assignmentSecondTimeEnd.setText("-");
                    }
                    assignmentSecondCodeTextView.setText(assignment.AssignmentCode);
                    assignmentSecondNoticesTextView.setText(assignment.Comments);
                    assignmentSecondStartLocation.setText(assignment.AssignmentStartLocation);
                    assignmentSecondEndLocation.setText(assignment.AssignmentEndLocation);

                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private static class updateAssignmentFirstTimeLeftRunnable implements Runnable{
        TextView assignmentFirstTimeLeft;
        Handler uiThreadHandler;

        public updateAssignmentFirstTimeLeftRunnable(TextView assignmentFirstTimeLeft, Handler uiThreadHandler) {
            this.assignmentFirstTimeLeft = assignmentFirstTimeLeft;
            this.uiThreadHandler = uiThreadHandler;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()){
                    uiThreadHandler.post(() -> assignmentFirstTimeLeft.setText(new AssignmentCountdownFinder(new AssignmentFinder(UsersLiveData.getCurrentlySelectedUserLiveData().getValue().Assignments).getFirstUpcomingAssignment()).getTimeLeft()));
                    Thread.sleep(15000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
