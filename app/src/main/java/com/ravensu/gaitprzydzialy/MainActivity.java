package com.ravensu.gaitprzydzialy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.ravensu.gaitprzydzialy.webscrapper.GAiTWebScrapper;
import com.ravensu.gaitprzydzialy.webscrapper.User;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper(new User("", ""));

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                    gAiTWebScrapper.ScrapAssignmentsTable();
            }
        }).start();




    }
}
