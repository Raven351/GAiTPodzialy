package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

public class AssignmentsTodayTomorrowFragment extends Fragment {

    private AssignmentsTodayTomorrowViewModel assignmentsTodayTomorrowViewModel;

    public static AssignmentsTodayTomorrowFragment newInstance() {
        return new AssignmentsTodayTomorrowFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.assignments_today_tomorrow_fragment, container, false);
        //todo mvvm not working, data not showing on view
        final TextView assignmentFirstDateTextView = view.findViewById(R.id.assignmentFirstDate);
        final TextView assignmentFirstCodeTextView = view.findViewById(R.id.assignmentFirstCode);
        assignmentsTodayTomorrowViewModel = new ViewModelProvider(this).get(AssignmentsTodayTomorrowViewModel.class);
        assignmentsTodayTomorrowViewModel.getFirstAssignment().observe(getViewLifecycleOwner(), new Observer<Assignment>() {
            @Override
            public void onChanged(Assignment assignment) {
                assignmentFirstDateTextView.setText(assignment.Date.toString());
                assignmentFirstCodeTextView.setText(assignment.AssignmentCode);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
