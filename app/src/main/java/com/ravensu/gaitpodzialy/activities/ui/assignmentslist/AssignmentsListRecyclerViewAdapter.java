package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.AssignmentsListFragment.OnListFragmentInteractionListener;
import com.ravensu.gaitpodzialy.dummy.DummyContent.DummyItem;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AssignmentsListRecyclerViewAdapter extends RecyclerView.Adapter<AssignmentsListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Assignment> assignments;
    private final OnListFragmentInteractionListener mListener;

    public AssignmentsListRecyclerViewAdapter(ArrayList<Assignment> assignments, OnListFragmentInteractionListener listener) {
        this.assignments = assignments;
        mListener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_assignments_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(position % 2 == 1) holder.mView.setBackgroundColor(Color.parseColor("#e8e8e8"));
        holder.mAssignment = assignments.get(position);
        String dateFormat = new SimpleDateFormat("dd-MM-yyyy").format(assignments.get(position).Date);
        holder.mDate.setText(dateFormat);
        holder.mAssignmentCode.setText(assignments.get(position).AssignmentCode);
        holder.mComments.setText(assignments.get(position).Comments);
        holder.mTimeStart.setText(assignments.get(position).AssignmentStartTime.toString());
        holder.mTimeEnd.setText(assignments.get(position).AssignmentEndTime.toString());
        holder.mLocationStart.setText(assignments.get(position).AssignmentStartLocation);
        holder.mLocationEnd.setText(assignments.get(position).AssignmentEndLocation);
        holder.mTimeTotal.setText(assignments.get(position).AssignmentDuration.toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mAssignment);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDate;
        public final TextView mAssignmentCode;
        public final TextView mComments; //notices
        public final TextView mTimeStart;
        public final TextView mTimeEnd;
        public final TextView mLocationStart;
        public final TextView mLocationEnd;
        public final TextView mTimeTotal;
        public Assignment mAssignment;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDate = (TextView) view.findViewById(R.id.date);
            mAssignmentCode = (TextView) view.findViewById(R.id.assignmentCode);
            mComments = (TextView) view.findViewById(R.id.comments);
            mTimeStart = (TextView) view.findViewById(R.id.timeStart);
            mTimeEnd = (TextView) view.findViewById(R.id.timeEnd);
            mLocationStart = (TextView) view.findViewById(R.id.locationStart);
            mLocationEnd = (TextView) view.findViewById(R.id.locationEnd);
            mTimeTotal = (TextView) view.findViewById(R.id.timeTotal);
        }

        @Override
        public String toString() {
            return super.toString() +
                    " '" + mDate.getText() +
                    ", " + mAssignmentCode.getText() +
                    ", " + mComments.getText() +
                    ", " + mTimeStart.getText() +
                    ", " + mTimeEnd.getText() +
                    ", " + mLocationStart.getText() +
                    ", " + mLocationEnd.getText() +
                    ", " + mTimeTotal.getText();
        }
    }
}
