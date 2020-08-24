package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import java.text.SimpleDateFormat;

public class AssignmentsFirstSecondFragment extends Fragment {

    private AssignmentsFirstSecondViewModel assignmentsFirstSecondViewModel;

    public static AssignmentsFirstSecondFragment newInstance() {
        return new AssignmentsFirstSecondFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.assignments_today_tomorrow_fragment, container, false);
        final TextView driverNameTextView = view.findViewById(R.id.driverName);
        final TextView driverIdTextView = view.findViewById(R.id.driverId);
        final TextView assignmentFirstStatusTextView = view.findViewById(R.id.assignmentFirstStatus);
        final TextView assignmentFirstDateTextView = view.findViewById(R.id.assignmentFirstDate);
        final TextView assignmentFirstCodeTextView = view.findViewById(R.id.assignmentFirstCode);
        final TextView assignmentFirstNoticesTextView = view.findViewById(R.id.assignmentFirstNotices);
        final TextView assignmentFirstTimeStart = view.findViewById(R.id.assignmentFirstTimeStart);
        final TextView assignmentFirstTimeEnd = view.findViewById(R.id.assignmentFirstTimeEnd);
        final TextView assignmentFirstStartLocation = view.findViewById(R.id.assignmentFirstStartLocation);
        final TextView assignmentFirstEndLocation = view.findViewById(R.id.assignmentFirstEndLocation);
        final TextView assignmentFirstTimeTotal = view.findViewById(R.id.assignmentFirstTimeTotal);
        final TextView assignmentSecondDateTextView = view.findViewById(R.id.assignmentSecondDate);
        final TextView assignmentSecondCodeTextView = view.findViewById(R.id.assignmentSecondCode);
        final TextView assignmentSecondNoticesTextView = view.findViewById(R.id.assignmentSecondNotices);
        final TextView assignmentSecondTimeStart = view.findViewById(R.id.assignmentSecondTimeStart);
        final TextView assignmentSecondTimeEnd = view.findViewById(R.id.assignmentSecondTimeEnd);
        final TextView assignmentSecondStartLocation = view.findViewById(R.id.assignmentSecondStartLocation);
        final TextView assignmentSecondEndLocation = view.findViewById(R.id.assignmentSecondEndLocation);
        final TextView assignmentSecondTimeTotal = view.findViewById(R.id.assignmentSecondTimeTotal);

        if (UsersData.getCurrentlySelectedUser().Assignments.size() == 0){
            driverIdTextView.setText(UsersData.getCurrentlySelectedUserId());
            driverNameTextView.setVisibility(View.GONE);
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
        }

        assignmentsFirstSecondViewModel = new ViewModelProvider(this).get(AssignmentsFirstSecondViewModel.class);
        assignmentsFirstSecondViewModel.getFirstAssignment().observe(getViewLifecycleOwner(), new Observer<Assignment>() {
            @Override
            public void onChanged(Assignment assignment) {
                driverIdTextView.setText(assignment.DriverNumber);
                driverNameTextView.setText(assignment.DriverName);
                String dateFormat = new SimpleDateFormat("dd-MM-yyyy").format(assignment.Date);
                assignmentFirstDateTextView.setText(dateFormat);
                assignmentFirstCodeTextView.setText(assignment.AssignmentCode);
                assignmentFirstNoticesTextView.setText(assignment.Comments);
                assignmentFirstTimeTotal.setText(assignment.AssignmentDuration.toString());
                assignmentFirstTimeStart.setText(assignment.AssignmentStartTime.toString());
                assignmentFirstStartLocation.setText(assignment.AssignmentStartLocation);
                assignmentFirstTimeEnd.setText(assignment.AssignmentEndTime.toString());
                assignmentFirstEndLocation.setText(assignment.AssignmentEndLocation);

            }
        });
        assignmentsFirstSecondViewModel.getIsOnGoing().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    assignmentFirstStatusTextView.setText(R.string.status_ongoing);
                    assignmentFirstStatusTextView.setTextColor(Color.parseColor("#1895f5"));
                }
                else {
                    assignmentFirstStatusTextView.setText(R.string.status_willstart);
                    assignmentFirstStatusTextView.setTextColor(Color.parseColor("#19851b"));
                }
            }
        });
        assignmentsFirstSecondViewModel.getSecondAssignment().observe(getViewLifecycleOwner(), new Observer<Assignment>() {
            @Override
            public void onChanged(Assignment assignment) {
                String dateFormat = new SimpleDateFormat("dd-MM-yyyy").format(assignment.Date);
                assignmentSecondDateTextView.setText(dateFormat);
                assignmentSecondCodeTextView.setText(assignment.AssignmentCode);
                assignmentSecondNoticesTextView.setText(assignment.Comments);
                assignmentSecondTimeTotal.setText(assignment.AssignmentDuration.toString());
                assignmentSecondTimeStart.setText(assignment.AssignmentStartTime.toString());
                assignmentSecondTimeEnd.setText(assignment.AssignmentEndTime.toString());
                assignmentSecondStartLocation.setText(assignment.AssignmentStartLocation);
                assignmentSecondEndLocation.setText(assignment.AssignmentEndLocation);
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
}
