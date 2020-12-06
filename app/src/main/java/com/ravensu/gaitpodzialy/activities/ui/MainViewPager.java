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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.activities.ui.accountsList.AccountsListActivity;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.AssignmentsListFragment;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.AssignmentsFirstSecondFragment;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.DocumentsListFragment;
import com.ravensu.gaitpodzialy.appdata.UsersLiveData;
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
        viewModel = new ViewModelProvider(this).get(MainViewPagerViewModel.class);
        super.onCreate(savedInstanceState);
        observeIsProperlyLoggedInLiveData();
    }

    private void observeIsProperlyLoggedInLiveData() {
        viewModel.getIsProperlyLoggedIn().observe(this, isProperlyLoggedIn -> {
            if (isProperlyLoggedIn){
                setUpLoggedInView();
            }
            else{
                setUpDataErrorView();
            }
        });
    }


    private void setUpDataErrorView() {
        setContentView(R.layout.activity_main_view_pager_data_error);
        setUpToolbar();
        showUserLoginFailedAlertDialog();
    }

    private void setUpLoggedInView() {
        setContentView(R.layout.activity_main_view_pager);
        setUpViewPager();
        setUpToolbar();
        observeDriverIdLiveData();
        observeDriverNameLiveData();
    }

    private void observeDriverNameLiveData() {
        final TextView driverNameTextView = findViewById(R.id.driverName);
        viewModel.getDriverName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                driverNameTextView.setText(s);
            }
        });
    }

    private void observeDriverIdLiveData() {
        final TextView driverIdTextView = findViewById(R.id.driverId);
        viewModel.getDriverId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                driverIdTextView.setText(s);
            }
        });
    }

    private void setUpViewPager() {
        viewPager2 = findViewById(R.id.assignments_list_viewpager);
        pageAdapter = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(pageAdapter);
        viewPager2.setCurrentItem(getIntent().getIntExtra("CURRENT_PAGE", 0), false);
        CircleIndicator3 viewpagerIndicator = findViewById(R.id.assignments_list_viewpager_indicator);
        viewpagerIndicator.setViewPager(viewPager2);
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
    }

    public void onClickInfoButton(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    //todo change
    public void onClickRefreshButton(View view) {
        mainViewpagerLoadingCircle = findViewById(R.id.mainViewpagerLoadingCircle);
        view.setVisibility(View.GONE);
        View refreshLoadingInProgressCircle = findViewById(R.id.refreshLoadingInProgressCircle);
        refreshLoadingInProgressCircle.setVisibility(View.VISIBLE);
        mainViewpagerLoadingCircle.setVisibility(View.VISIBLE);
        viewPager2.setUserInputEnabled(false);
        findViewById(R.id.infoButton).setEnabled(false);
        findViewById(R.id.accountsButton).setEnabled(false);
        findViewById(R.id.refreshButton).setEnabled(false);
        final String currentlySelectedUserId = UsersLiveData.getCurrentlySelectedUserLiveData().getValue().UserId;
        if (refreshData(currentlySelectedUserId)){

            mainViewpagerLoadingCircle.setVisibility(View.GONE);
            refreshLoadingInProgressCircle.setVisibility(View.GONE);
            viewPager2.setUserInputEnabled(true);
            findViewById(R.id.infoButton).setEnabled(true);
            findViewById(R.id.accountsButton).setEnabled(true);
            findViewById(R.id.refreshButton).setEnabled(true);
        }

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

    private boolean refreshData(String currentlySelectedUserId){
        boolean usersDataLoaded = UsersLiveData.loadUsersData(this);
        if (usersDataLoaded) {
            UsersLiveData.setCurrentlySelectedUser(UsersLiveData.getUsersLiveData().getValue().get(currentlySelectedUserId));
        }
        else{
            usersLoadingErrorDialog();
        }
        return true;
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
