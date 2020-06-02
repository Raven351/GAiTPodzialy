package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ravensu.gaitpodzialy.R;

public class AssignmentsListCurrentsFragment extends Fragment {

    private AssignmentsListCurrentsViewModelFragment mViewModel;

    public static AssignmentsListCurrentsFragment newInstance() {
        return new AssignmentsListCurrentsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.assignments_list_currents_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AssignmentsListCurrentsViewModelFragment.class);
        // TODO: Use the ViewModel
    }

}
