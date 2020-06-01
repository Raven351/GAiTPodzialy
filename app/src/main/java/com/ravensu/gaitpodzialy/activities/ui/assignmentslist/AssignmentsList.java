package com.ravensu.gaitpodzialy.activities.ui.assignmentslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.ravensu.gaitpodzialy.R;

public class AssignmentsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments_list);
        setUpToolbar();
    }

    private boolean isPreviousActivityMain(){
        Intent intent = getIntent();
        String previousActivity = intent.getStringExtra("FROM_ACTIVITY");
        return previousActivity.equals("MAIN");
    }

    @Override
    public void onBackPressed(){
        if (isPreviousActivityMain()){
        }
        else{
            super.onBackPressed();
        }
    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_assignments_list_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
    }
}
