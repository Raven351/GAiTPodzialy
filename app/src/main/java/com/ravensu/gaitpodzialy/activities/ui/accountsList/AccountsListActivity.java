package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.activities.ui.login.LoginActivity;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.concurrent.ConcurrentHashMap;

public class AccountsListActivity extends AppCompatActivity {
    private AccountsListViewModel accountsListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_list);
        RecyclerView recyclerView = findViewById(R.id.accountsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpToolbar();
        final AccountsListAdapter adapter = new AccountsListAdapter(this);
        recyclerView.setAdapter(adapter);
        accountsListViewModel = new ViewModelProvider(this).get(AccountsListViewModel.class);
        accountsListViewModel.getUsers().observe(this, adapter::setUsers);
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
                try {
                    UsersData.loadUsersData(this);
                    finish();
                    startActivity(getIntent());
                }
                catch (InterruptedException e){

                }
            }
        }
    }
}
