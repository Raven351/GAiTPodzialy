package com.ravensu.gaitpodzialy.activities.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
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
        setupVersionName();
    }

    private void setupVersionName() {
        TextView textViewVersion = findViewById(R.id.version);
        try {
            textViewVersion.append(this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupHyperlinks(){
        TextView textViewMadeBy = findViewById(R.id.textview_made_by);
        TextView textViewAbout = findViewById(R.id.textview_about);
        textViewMadeBy.setMovementMethod(LinkMovementMethod.getInstance());
        textViewAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }
}