package com.ravensu.gaitpodzialy.activities.ui.accountslist;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.Collection;

public class AccountsListAdapter extends RecyclerView.Adapter<AccountsListAdapter.ViewHolder> {

    private ArrayList<User> users;
    private AccountsListViewModel viewModel;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logins_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position % 2 == 1) holder.mView.setBackgroundColor(Color.parseColor("#e8e8e8"));
        holder.mUser = users.get(position);
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public User mUser;
        public final View mView;
        public final TextView mUserId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
            this.mUserId = itemView.findViewById(R.id.userId);
        }
    }
}
