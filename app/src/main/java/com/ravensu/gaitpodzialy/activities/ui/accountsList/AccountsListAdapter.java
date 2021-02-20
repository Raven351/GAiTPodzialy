package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.MainActivity;
import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.appdata.SavedAppLogins;
import com.ravensu.gaitpodzialy.appdata.SavedAppMainLogin;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.dialogs.ConfirmGeneralDialogFragment;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AccountsListAdapter extends RecyclerView.Adapter<AccountsListAdapter.ViewHolder> {

    private final ArrayList<User> users = new ArrayList<User>();
    private final Activity parentActivity;
    private final FragmentManager fragmentManager;
    private AccountsListViewModel viewModel;

    public AccountsListAdapter(Activity activity, FragmentManager fragmentManager){
        super();
        this.parentActivity = activity;
        this.fragmentManager = fragmentManager;
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
        if (users.get(position) == UsersLiveData.getCurrentlySelectedUserLiveData().getValue()) holder.mView.setBackgroundColor(parentActivity.getResources().getColor(R.color.selectedUser));
        if (users.get(position) == UsersLiveData.getMainUserLiveData().getValue()) {
            holder.mUserId.setTypeface(null, Typeface.BOLD);
            holder.mUserId.setTextColor(parentActivity.getResources().getColor(R.color.mainUser));
        }

        holder.mUser = users.get(position);
        holder.mUserId.setText(users.get(position).UserId);
        holder.mChangeToUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                UsersLiveData.setCurrentlySelectedUser(users.get(position));
                parentActivity.finish();
            }
        });
        holder.mSetAsMainUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("UserId", users.get(position).UserId);
                ConfirmGeneralDialogFragment.newInstance(users.get(position).UserId ,parentActivity.getString(R.string.change_default_user_alert_dialog_message), args).show(fragmentManager, "ConfirmSetAsMainUser");
            }
        });

        holder.mLogOutUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemCount() > 1 && !canBeLogOut(users.get(position).UserId)){
                    Toast.makeText(parentActivity, R.string.unable_to_logout_toast, Toast.LENGTH_LONG).show();
                    return;
                }
                if (users.get(position).UserId.equals(UsersLiveData.getCurrentlySelectedUserLiveData().getValue().UserId)) UsersLiveData.setCurrentlySelectedUser(UsersLiveData.getMainUserLiveData().getValue());
                new AlertDialog.Builder(parentActivity)
                        .setTitle(users.get(position).UserId)
                        .setMessage(R.string.logout_confirmation_alert_dialog_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SavedAppLogins.removeCredentials(parentActivity, users.get(position).UserId);
                                UsersLiveData.removeUserData(users.get(position).UserId);
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
        //else return !userId.equals(UsersData.getCurrentlySelectedUserId());
        return !userId.equals(UsersLiveData.getMainUserLiveData().getValue().UserId);
    }
}
