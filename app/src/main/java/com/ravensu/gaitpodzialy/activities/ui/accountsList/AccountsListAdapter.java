package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.MainActivity;
import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.data.SavedAppLogins;
import com.ravensu.gaitpodzialy.data.SavedAppMainLogin;
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
        if (position % 2 == 1) holder.mView.setBackgroundColor(parentActivity.getResources().getColor(R.color.greyList));
        if (users.get(position) == UsersData.getCurrentlySelectedUser()) holder.mView.setBackgroundColor(parentActivity.getResources().getColor(R.color.selectedUser));
        if (users.get(position) == UsersData.getMainUser()) {
            holder.mUserId.setTypeface(null, Typeface.BOLD);
            holder.mUserId.setTextColor(parentActivity.getResources().getColor(R.color.mainUser));
        }

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
        holder.mSetAsMainUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parentActivity)
                        .setTitle(users.get(position).UserId)
                        .setMessage(R.string.change_default_user_alert_dialog_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UsersData.setMainUser(users.get(position).UserId);
                                SavedAppMainLogin.SetMainLoginUserId(parentActivity, users.get(position).UserId);
                                if (users.get(position).Assignments.size()>0)
                                    SavedAppMainLogin.SetMainLoginUserName(parentActivity, users.get(position).Assignments.get(0).DriverName);
                                parentActivity.finish();
                                parentActivity.startActivity(parentActivity.getIntent());
                                Toast.makeText(parentActivity, (parentActivity.getString(R.string.change_default_user_success_toast_1)) + " " + users.get(position).UserId + " " + (parentActivity.getString(R.string.change_default_user_success_toast_2)), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        holder.mLogOutUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemCount() > 1 && !canBeLogOut(users.get(position).UserId)){
                    Toast.makeText(parentActivity, R.string.unable_to_logout_toast, Toast.LENGTH_LONG).show();
                    return;
                }
                if (users.get(position).UserId.equals(UsersData.getCurrentlySelectedUserId())) UsersData.setCurrentlySelectedUser(UsersData.getMainUser().UserId);
                new AlertDialog.Builder(parentActivity)
                        .setTitle(users.get(position).UserId)
                        .setMessage(R.string.logout_confirmation_alert_dialog_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SavedAppLogins.RemoveCredentials(parentActivity, users.get(position).UserId);
                                UsersData.removeUser(users.get(position).UserId);
                                users.remove(users.get(position));
                                if (getItemCount() == 0){
                                    Intent intent = new Intent(parentActivity, MainActivity.class);
                                    parentActivity.startActivity(intent);
                                    parentActivity.finish();
                                    parentActivity.finishAffinity();
                                }
                                else{
                                    parentActivity.finish();
                                    parentActivity.startActivity(parentActivity.getIntent());
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
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
        public final ImageButton mSetAsMainUserButton;
        public final ImageButton mLogOutUserButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
            this.mUserId = itemView.findViewById(R.id.userId);
            this.mChangeToUserButton = itemView.findViewById(R.id.changeToUserButton);
            this.mSetAsMainUserButton = itemView.findViewById(R.id.setAsMainUserButton);
            this.mLogOutUserButton = itemView.findViewById(R.id.logOutUserButton);
        }
    }

    private boolean canBeLogOut(String userId){
        if (userId.equals(UsersData.getMainUser().UserId)) return false;
        //else return !userId.equals(UsersData.getCurrentlySelectedUserId());
        else return true;
    }
}
