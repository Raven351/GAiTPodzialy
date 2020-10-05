package com.ravensu.gaitpodzialy.activities.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.ravensu.gaitpodzialy.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupHyperlinks();
    }

    private void setupHyperlinks(){
        TextView textViewMadeBy = findViewById(R.id.textview_made_by);
        TextView textViewAbout = findViewById(R.id.textview_about);
        TextView textViewAboutMoreInfo = findViewById(R.id.textview_about_more_info);
        textViewMadeBy.setMovementMethod(LinkMovementMethod.getInstance());
        textViewAbout.setMovementMethod(LinkMovementMethod.getInstance());
        textViewAboutMoreInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }
}