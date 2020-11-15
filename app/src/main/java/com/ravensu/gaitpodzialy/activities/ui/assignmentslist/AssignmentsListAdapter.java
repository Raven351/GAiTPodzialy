package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

public class AssignmentsListAdapter extends RecyclerView.Adapter<AssignmentsListAdapter.ViewHolder> {

    private ArrayList<Assignment> assignments;
    private int todayAssignmentPosition;

    public AssignmentsListAdapter() {
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
        LocalDate today = LocalDate.now();
        if (assignments.get(position).AssignmentStartDateTime.toLocalDate().equals(today)) {
            holder.mView.setBackgroundColor(Color.parseColor("#aacef0"));
            todayAssignmentPosition = position;
        }
        else if(position % 2 == 1) holder.mView.setBackgroundColor(Color.parseColor("#e8e8e8"));
        else holder.mView.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.mAssignment = assignments.get(position);
        String dateFormat = assignments.get(position).AssignmentStartDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        holder.mDate.setText(dateFormat);
        holder.mAssignmentCode.setText(assignments.get(position).AssignmentCode);
        holder.mComments.setText(assignments.get(position).Comments);
        holder.mTimeStart.setText(assignments.get(position).AssignmentStartDateTime.toLocalTime().toString());
        holder.mTimeEnd.setText(assignments.get(position).AssignmentEndDateTime.toLocalTime().toString());
        holder.mLocationStart.setText(assignments.get(position).AssignmentStartLocation);
        holder.mLocationEnd.setText(assignments.get(position).AssignmentEndLocation);
        holder.mTimeTotal.setText(assignments.get(position).AssignmentDuration.toString());
        String weekday = assignments.get(position).AssignmentStartDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("EEEE"));
        weekday = weekday.substring(0, 1).toUpperCase() + weekday.substring(1);
        holder.mWeekday.setText(weekday);
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public int getTodayAssignmentPosition() {return todayAssignmentPosition;}

    public void setAssignments(ArrayList<Assignment> assignments){
        this.assignments = assignments;
        notifyDataSetChanged();
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
        public final TextView mWeekday;
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
            mWeekday = (TextView) view.findViewById(R.id.weekday);
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
