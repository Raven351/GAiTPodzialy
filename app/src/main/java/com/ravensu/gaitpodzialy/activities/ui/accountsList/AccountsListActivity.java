package com.ravensu.gaitpodzialy.activities.ui.accountsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.concurrent.ConcurrentHashMap;

public class AccountsListActivity extends AppCompatActivity {
    private AccountsListViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_list);
        RecyclerView recyclerView = findViewById(R.id.accountsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpToolbar();
        final AccountsListAdapter adapter = new AccountsListAdapter();
        recyclerView.setAdapter(adapter);
        //viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AccountsListViewModel.class);
        viewModel = new ViewModelProvider(this).get(AccountsListViewModel.class);
        viewModel.getUsers().observe(this, new Observer<ConcurrentHashMap<String, User>>() {
            @Override
            public void onChanged(ConcurrentHashMap<String, User> users) {
                adapter.setUsers(users);
            }
        });

    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_accounts_list_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
    }

    public void onClickAddAccountButton(View view) {

    }
}
