package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ravensu.gaitpodzialy.MainActivity;
import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.appdata.SavedAppLogins;
import com.ravensu.gaitpodzialy.appdata.SavedAppMainLogin;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.appdata.sorters.UsersSorter;
import com.ravensu.gaitpodzialy.dialogs.ConfirmGeneralDialogFragment;
import com.ravensu.gaitpodzialy.webscrapper.data.DriverTypeFinder;
import com.ravensu.gaitpodzialy.webscrapper.enums.DriverType;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AccountsListAdapter extends RecyclerView.Adapter<AccountsListAdapter.ViewHolder>{
    private final String TAG = "AccountsListAdapter";
    private ArrayList<User> users = new ArrayList<User>();
    private final Activity parentActivity;
    private final FragmentManager fragmentManager;
    private AccountsListViewModel viewModel;
    private ViewHolder mainUserHolder;
    private int mainUserHolderPosition;
    private int currentlySelectedUserHolderPosition;
    private int clickHolderPosition;

    public AccountsListAdapter(Activity activity, FragmentManager fragmentManager){
        super();
        this.parentActivity = activity;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logins_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        sortUsers(users);
        if (position % 2 == 1) holder.mView.setBackgroundColor(parentActivity.getResources().getColor(R.color.greyList));
        if (users.get(position).UserId.equals(UsersLiveData.getCurrentlySelectedUserLiveData().getValue().UserId)) {
            setUpCurrentlySelectedUserHolder(holder, position);
        }
        if (users.get(position) == UsersLiveData.getMainUserLiveData().getValue()) {
            setUpMainUserHolder(holder, position);
        }
        holder.mUser = users.get(position);
        holder.mUserId.setText(users.get(position).UserId);
        if (!users.get(position).Assignments.isEmpty()){
            holder.mUserName.setText(users.get(position).Assignments.get(0).DriverName);
        }
        if(new DriverTypeFinder(users.get(position).UserId).getDriverType().equals(DriverType.TRAM)) holder.mDriverTypeIcon.setImageResource(R.drawable.tram_icon);
        holder.mView.setOnClickListener(v -> {
            clickHolderPosition = position;
            selectUser();
        });
        holder.mShowUsersOptionsButton.setOnClickListener(v -> {
            clickHolderPosition = position;
            holder.showUserListItemMenu(v);
        });
    }

    public void selectUser() {
        UsersLiveData.setCurrentlySelectedUser(users.get(clickHolderPosition));
        parentActivity.finish();
    }

    private void sortUsers(ArrayList<User> users) { //settings possible
        UsersSorter.sortById(users);
    }

    private void setUpMainUserHolder(ViewHolder holder, int position) {
        holder.mDriverTypeIcon.setColorFilter(parentActivity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mainUserHolder = holder;
        mainUserHolderPosition = position;
    }

    private void removeMainUserStatusFromHolder(){
        mainUserHolder.mDriverTypeIcon.setColorFilter(null);
    }

    public void refreshMainUserHolderStyling(){
        removeMainUserStatusFromHolder();
        notifyDataSetChanged();
    }

    private void setUpCurrentlySelectedUserHolder(ViewHolder holder, int position) {
        holder.mView.setBackgroundColor(parentActivity.getResources().getColor(R.color.selectedUser));
        currentlySelectedUserHolderPosition = position;

    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public void setUsers(ConcurrentHashMap<String, User> users){
        this.users.clear();
        for (String key : users.keySet()){
            this.users.add(users.get(key));
        }
        notifyDataSetChanged();
    }

    public void removeUser(String userId){
        User userToRemove = new User();
        for (User user : users){
            if (user.UserId.equals(userId)) userToRemove = user;
        }
        users.remove(userToRemove);
    }

    public void logoutUser(String userId){
        if (users.get(clickHolderPosition).UserId.equals(UsersLiveData.getCurrentlySelectedUserLiveData().getValue().UserId)) UsersLiveData.setCurrentlySelectedUser(UsersLiveData.getMainUserLiveData().getValue());
        removeUser(userId);
    }

    public void onClickSetAsMainUser(){
        Bundle args = new Bundle();
        args.putString("UserId", users.get(clickHolderPosition).UserId);
        ConfirmGeneralDialogFragment.newInstance(users.get(clickHolderPosition).UserId, parentActivity.getString(R.string.change_default_user_alert_dialog_message), args).show(fragmentManager, "ConfirmSetAsMainUser");
    }

    public void onClickLogoutUser(){
        if (getItemCount() > 1 && !canBeLogOut(users.get(currentlySelectedUserHolderPosition).UserId)){
            Toast.makeText(parentActivity, R.string.unable_to_logout_toast, Toast.LENGTH_LONG).show();
        }
        else {
            Bundle args = new Bundle();
            args.putString("UserId", users.get(currentlySelectedUserHolderPosition).UserId);
            ConfirmGeneralDialogFragment.newInstance(users.get(currentlySelectedUserHolderPosition).UserId, parentActivity.getString(R.string.logout_confirmation_alert_dialog_message), args).show(fragmentManager, "ConfirmLogoutUser");
        }
    }

    private boolean canBeLogOut(String userId){
        //else return !userId.equals(UsersData.getCurrentlySelectedUserId());
        return !userId.equals(UsersLiveData.getMainUserLiveData().getValue().UserId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public User mUser;
        public final View mView;
        public final TextView mUserId;
        public final TextView mUserName;
        public final ImageView mDriverTypeIcon;
        public final ImageButton mShowUsersOptionsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
            this.mUserId = itemView.findViewById(R.id.userId);
            this.mUserName = itemView.findViewById(R.id.userName);
            this.mDriverTypeIcon = itemView.findViewById(R.id.driverTypeIcon);
            this.mShowUsersOptionsButton = itemView.findViewById(R.id.userOptionsMenuButton);
        }

        private void showUserListItemMenu(View v){
            PopupMenu popupMenu = new PopupMenu(parentActivity, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.user_list_item_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) parentActivity);
            popupMenu.show();
        }
    }
}
