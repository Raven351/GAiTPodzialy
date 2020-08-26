package com.ravensu.gaitpodzialy.activities.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.ravensu.gaitpodzialy.R;
import com.ravensu.gaitpodzialy.activities.ui.accountsList.AccountsListActivity;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.AssignmentsListFragment;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.AssignmentsFirstSecondFragment;
import com.ravensu.gaitpodzialy.activities.ui.assignmentslist.DocumentsListFragment;
import com.ravensu.gaitpodzialy.data.UsersData;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

//https://developer.android.com/training/animation/screen-slide-2?hl=en#java

public class MainViewPager extends AppCompatActivity implements AssignmentsListFragment.OnListFragmentInteractionListener {

    private static final int NUM_PAGES = 3;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!UsersData.getCurrentlySelectedUser().isUserProperlyLoggedIn){
            setContentView(R.layout.activity_main_view_pager_data_error);
            setUpToolbar();
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
        else{
            setContentView(R.layout.activity_main_view_pager);
            setUpToolbar();
            viewPager2 = findViewById(R.id.assignments_list_viewpager);
            pageAdapter = new ScreenSlidePagerAdapter(this);
            viewPager2.setAdapter(pageAdapter);
            viewPager2.setCurrentItem(getIntent().getIntExtra("CURRENT_PAGE", 0), false);
        }
    }

    private boolean isPreviousActivityMain(){
        Intent intent = getIntent();
        String previousActivity = intent.getStringExtra("FROM_ACTIVITY");
        return previousActivity.equals("MAIN");
    }

    @Override
    public void onBackPressed(){
        if (viewPager2 != null){
            if (isPreviousActivityMain() || viewPager2.getCurrentItem() == 0){
                //do nothing
            }
            else{
                super.onBackPressed();
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
            }
        }
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
}
