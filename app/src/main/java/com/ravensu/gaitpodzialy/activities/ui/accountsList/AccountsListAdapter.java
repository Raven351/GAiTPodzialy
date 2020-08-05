package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.data.SavedAppLogins;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AccountsListAdapter extends RecyclerView.Adapter<AccountsListAdapter.ViewHolder> {

    private ArrayList<User> users = new ArrayList<User>();
    private Activity parentActivity;
    private AccountsListViewModel viewModel;

    public AccountsListAdapter(Activity activity){
        super();
        this.parentActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logins_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position % 2 == 1) holder.mView.setBackgroundColor(Color.parseColor("#e8e8e8"));
        if (users.get(position) == UsersData.getCurrentlySelectedUser()) holder.mView.setBackgroundColor(Color.parseColor("#0ecef0"));
        holder.mUser = users.get(position);
        holder.mUserId.setText(users.get(position).UserId);
        holder.mChangeToUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                parentActivity.findViewById(R.id.accountsLoadingCircle).setVisibility(View.VISIBLE);
                UsersData.setCurrentlySelectedUser(users.get(position).UserId);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        parentActivity.findViewById(R.id.accountsLoadingCircle).setVisibility(View.GONE);
                        parentActivity.finish();
                    }
                }, 500); //delay added because if active user is being changed too fast it will cause assignments views to not refresh itself and show assignments for previously selected user

            }
        });
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public void setUsers(ConcurrentHashMap<String, User> users){
        for (String key : users.keySet()){
            this.users.add(users.get(key));
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public User mUser;
        public final View mView;
        public final TextView mUserId;
        public final ImageButton mChangeToUserButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
            this.mUserId = itemView.findViewById(R.id.userId);
            this.mChangeToUserButton = itemView.findViewById(R.id.changeToUserButton);
        }
    }
}
