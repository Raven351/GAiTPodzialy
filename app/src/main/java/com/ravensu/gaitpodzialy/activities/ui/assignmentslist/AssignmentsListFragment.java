package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.jetbrains.annotations.NotNull;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AssignmentsListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private AssignmentsListViewModel assignmentsListViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AssignmentsListFragment() {
    }

    public static AssignmentsListFragment newInstance(int columnCount) {
        AssignmentsListFragment fragment = new AssignmentsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignments_list, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.assignmentsList);
        Button showTodayButton = view.findViewById(R.id.showTodayButton);
        TextView assignmentsListInfoTextView = view.findViewById(R.id.assignmentsListInfoTextView);
        if (UsersLiveData.getCurrentlySelectedUserLiveData().getValue().Assignments.size() < 1){
            assignmentsListInfoTextView.setText(R.string.no_assignments);
            assignmentsListInfoTextView.setVisibility(View.VISIBLE);
        }
        else{
            assignmentsListInfoTextView.setVisibility(View.GONE);
        }
        // Set the adapter
        if (recyclerView != null) {
            Context context = view.getContext();
            final AssignmentsListAdapter adapter = new AssignmentsListAdapter(UsersLiveData.getCurrentlySelectedUserLiveData().getValue().Assignments);
            recyclerView.setAdapter(adapter);
            assignmentsListViewModel = new ViewModelProvider(this).get(AssignmentsListViewModel.class);
            assignmentsListViewModel.getAssignments().observe(getViewLifecycleOwner(), adapter::setAssignments);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                showTodayButton.setOnClickListener(v -> recyclerView.post(() -> recyclerView.smoothScrollToPosition(adapter.getTodayAssignmentPosition())));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }



    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Assignment assignment);
    }
}
