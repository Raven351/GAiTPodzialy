package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.ravensu.gaitpodzialy.MainActivity;
import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.activities.ui.login.LoginActivity;
import com.ravensu.gaitpodzialy.appdata.SavedAppLogins;
import com.ravensu.gaitpodzialy.appdata.SavedAppMainLogin;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.dialogs.ConfirmGeneralDialogFragment;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

public class AccountsListActivity extends AppCompatActivity implements ConfirmGeneralDialogFragment.onDialogFragmentClickListener, PopupMenu.OnMenuItemClickListener {
    private final String TAG = "AccountsListActivity";
    private AccountsListViewModel accountsListViewModel;
    private RecyclerView recyclerView;
    private AccountsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_list);
        recyclerView = findViewById(R.id.accountsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpToolbar();
        adapter = new AccountsListAdapter(this, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
        accountsListViewModel = new ViewModelProvider(this).get(AccountsListViewModel.class);
        accountsListViewModel.getUsersLiveData().observe(this, adapter::setUsers);
    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_accounts_list_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
    }

    public void onClickAddAccountButton(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("requestCode", 2);
        startActivityForResult(intent, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK){
                UsersLiveData.loadUsersData(this);
            }
        }
    }


    @Override
    public void onPositiveClicked(ConfirmGeneralDialogFragment dialogFragment) {
        String dialogTag = dialogFragment.getTag();
        switch (dialogTag){
            case "ConfirmSetAsMainUser":
                String userId = dialogFragment.getArguments().getString("UserId");
                setAsMainUser(userId);
                adapter.refreshMainUserHolderStyling();
                break;
            case "ConfirmLogoutUser":
                userId = dialogFragment.getArguments().getString("UserId");
                adapter.logoutUser(userId);
                SavedAppLogins.removeCredentials(this, userId);
                UsersLiveData.removeUserData(userId);
                if (adapter.getItemCount() == 0){
                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);
                    this.finish();
                    this.finishAffinity();
                }
                break;
            default:

        }
    }

    @Override
    public void onNegativeClicked(ConfirmGeneralDialogFragment dialogFragment) {

    }

    private void setAsMainUser(String userId) {
        User user = UsersLiveData.getUsersLiveData().getValue().get(userId);
        accountsListViewModel.setMainUser(user);
        SavedAppMainLogin.SetMainLoginUserId(this, userId);
        if (user.Assignments.size()>0)
            SavedAppMainLogin.SetMainLoginUserName(this, user.Assignments.get(0).DriverName);
        Toast.makeText(this, (this.getString(R.string.change_default_user_success_toast_1)) + " " + userId + " " + (this.getString(R.string.change_default_user_success_toast_2)), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.selectUserMenuItem:
                Log.d(TAG, "onMenuItemClick: clicked");
                adapter.selectUser();
                return true;
            case R.id.setAsDefaultUserMenuItem:
                adapter.onClickSetAsMainUser();
                return true;
            case R.id.logOutUserMenuItem:
                adapter.onClickLogoutUser();
                return true;
            default:
                return false;
        }
    }
}
