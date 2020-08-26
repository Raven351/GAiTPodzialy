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
        setContentView(R.layout.activity_main_view_pager);
        setUpToolbar();
        viewPager2 = findViewById(R.id.assignments_list_viewpager);
        pageAdapter = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(pageAdapter);
        viewPager2.setCurrentItem(getIntent().getIntExtra("CURRENT_PAGE", 0), false);
        if (!UsersData.getIsUsersDataAccessAvailable()){
            new AlertDialog.Builder(this)
                    .setTitle("Couldn't log in to GAiT Website")
                    .setMessage("Application couldn't login to GAiT website. Try again later or try logging in directly to GAiT website. If you could login properly to GAiT website and this message keeps popping up, please contact me at ...")
                    .setPositiveButton("Ok", null)
                    .setNegativeButton("Go to GAiT Website", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url = "http://podzialy.gait.pl/";
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    private boolean isPreviousActivityMain(){
        Intent intent = getIntent();
        String previousActivity = intent.getStringExtra("FROM_ACTIVITY");
        return previousActivity.equals("MAIN");
    }

    @Override
    public void onBackPressed(){
        if (isPreviousActivityMain() || viewPager2.getCurrentItem() == 0){
        }
        else{
            super.onBackPressed();
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
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
        finish();
        startActivity(getIntent().putExtra("CURRENT_PAGE", this.viewPager2.getCurrentItem()));
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
