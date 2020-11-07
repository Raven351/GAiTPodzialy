package com.ravensu.gaitpodzialy.activities.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.MainActivity;
import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.activities.ui.accountsList.AccountsListActivity;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.AssignmentsListFragment;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.AssignmentsFirstSecondFragment;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.DocumentsListFragment;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.data.UsersLiveData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import me.relex.circleindicator.CircleIndicator3;

//https://developer.android.com/training/animation/screen-slide-2?hl=en#java

public class MainViewPager extends AppCompatActivity implements AssignmentsListFragment.OnListFragmentInteractionListener {

    private static final int NUM_PAGES = 3;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pageAdapter;
    private MainViewPagerViewModel viewModel;
    private RelativeLayout mainViewpagerLoadingCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!UsersLiveData.getCurrentlySelectedUserLiveData().getValue().isUserProperlyLoggedIn){
            setContentView(R.layout.activity_main_view_pager_data_error);
            setUpToolbar();
            showUserLoginFailedAlertDialog();
        }
        else{
            setContentView(R.layout.activity_main_view_pager);
            setUpToolbar();
            viewPager2 = findViewById(R.id.assignments_list_viewpager);
            pageAdapter = new ScreenSlidePagerAdapter(this);
            viewPager2.setAdapter(pageAdapter);
            viewPager2.setCurrentItem(getIntent().getIntExtra("CURRENT_PAGE", 0), false);
            CircleIndicator3 viewpagerIndicator = findViewById(R.id.assignments_list_viewpager_indicator);
            viewpagerIndicator.setViewPager(viewPager2);
            final TextView driverIdTextView = findViewById(R.id.driverId);
            final TextView driverNameTextView = findViewById(R.id.driverName);
            viewModel = new ViewModelProvider(this).get(MainViewPagerViewModel.class);
            viewModel.getDriverId().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    driverIdTextView.setText(s);
                }
            });
            viewModel.getDriverName().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    driverNameTextView.setText(s);
                }
            });
        }
    }

    private void showUserLoginFailedAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.data_error_dialog_title)
                .setMessage(R.string.data_error_dialog_message)
                .setPositiveButton("OK", null)
                .setNegativeButton(R.string.data_error_dialog_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://podzialy.gait.pl/";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }).show();
    }

    private boolean isPreviousActivityMain(){
        Intent intent = getIntent();
        String previousActivity = intent.getStringExtra("FROM_ACTIVITY");
        return previousActivity.equals("MAIN");
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_app_dialog_title)
                .setMessage(R.string.exit_app_dialog_message)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.dialog_no, null)
                .show();
    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_assignments_list_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
    }

    @Override
    public void onListFragmentInteraction(Assignment assignment) {

    }

    public void onClickAccountsButton(View view) {
        Intent intent = new Intent(this, AccountsListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (viewPager2 != null){
            finish();
            startActivity(getIntent().putExtra("CURRENT_PAGE", this.viewPager2.getCurrentItem()));
        }
        else {
            finish();
            startActivity(getIntent());
        }
    }

    public void onClickInfoButton(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void onClickRefreshButton(View view) {
        mainViewpagerLoadingCircle = findViewById(R.id.mainViewpagerLoadingCircle);
        mainViewpagerLoadingCircle.setVisibility(View.VISIBLE);
        viewPager2.setUserInputEnabled(false);
        findViewById(R.id.infoButton).setEnabled(false);
        findViewById(R.id.accountsButton).setEnabled(false);
        findViewById(R.id.refreshButton).setEnabled(false);
        final String currentlySelectedUserId = UsersData.getCurrentlySelectedUserId();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                refreshData(currentlySelectedUserId);
            }
        });
        thread.start();
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter{
        public ScreenSlidePagerAdapter(FragmentActivity fragmentActivity){
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:{
                    return new AssignmentsFirstSecondFragment();
                }
                case 1: {
                    return new AssignmentsListFragment();
                }
                case 2:{
                    return new DocumentsListFragment();
                }
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

    }

    private void refreshData(String currentlySelectedUserId){
        try {
            boolean usersDataLoaded = UsersData.loadUsersData(this);
            if (usersDataLoaded) {
                finish();
                UsersData.setCurrentlySelectedUser(currentlySelectedUserId);
                startActivity(getIntent().putExtra("CURRENT_PAGE", this.viewPager2.getCurrentItem()));
            }
            else{
                usersLoadingErrorDialog();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            usersLoadingErrorDialog();
        }
    }

    private void usersLoadingErrorDialog(){
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.users_loading_error_dialog_title)
                .setMessage(R.string.users_loading_error_dialog_message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.data_error_dialog_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://podzialy.gait.pl/";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        finishAffinity();
                    }
                }).show();
    }
}
