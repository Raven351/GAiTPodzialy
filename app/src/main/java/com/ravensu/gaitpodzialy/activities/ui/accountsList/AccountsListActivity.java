package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.activities.ui.login.LoginActivity;
import com.ravensu.gaitpodzialy.appdata.SavedAppMainLogin;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
import com.ravensu.gaitpodzialy.dialogs.ConfirmGeneralDialogFragment;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

public class AccountsListActivity extends AppCompatActivity implements ConfirmGeneralDialogFragment.onDialogFragmentClickListener {
    private AccountsListViewModel accountsListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_list);
        RecyclerView recyclerView = findViewById(R.id.accountsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpToolbar();
        final AccountsListAdapter adapter = new AccountsListAdapter(this, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
        accountsListViewModel = new ViewModelProvider(this).get(AccountsListViewModel.class);
        accountsListViewModel.getUsers().observe(this, users -> {
            adapter.setUsers(users);
        });
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
                finish();
                startActivity(getIntent());
            }
        }
    }


    @Override
    public void onPositiveClicked(ConfirmGeneralDialogFragment dialogFragment) {
        String dialogTag = dialogFragment.getTag();
        switch (dialogTag){
            case "SelectUser":
                break;
            case "ConfirmSetAsMainUser":
                String userId = dialogFragment.getArguments().getString("UserId");
                setAsMainUser(userId);
                break;
            case "LogoutUser":
                break;
            default:

        }
    }

    @Override
    public void onNegativeClicked(ConfirmGeneralDialogFragment dialogFragment) {

    }

    private void setAsMainUser(String userId) {
        User user = UsersLiveData.getUsersLiveData().getValue().get(userId);
        UsersLiveData.setMainUser(user);
        SavedAppMainLogin.SetMainLoginUserId(this, userId);
        if (user.Assignments.size()>0)
            SavedAppMainLogin.SetMainLoginUserName(this, user.Assignments.get(0).DriverName);
        this.finish();
        this.startActivity(this.getIntent());
        Toast.makeText(this, (this.getString(R.string.change_default_user_success_toast_1)) + " " + userId + " " + (this.getString(R.string.change_default_user_success_toast_2)), Toast.LENGTH_SHORT).show();
    }

}
