package com.ravensu.gaitprzydzialy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ravensu.gaitprzydzialy.webscrapper.GAiTWebScrapper;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper("", "");

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try{
                    gAiTWebScrapper.ScrapAssignmentsTable(gAiTWebScrapper.GetGAiTWebsite());
                }catch(NullPointerException e){
                    Log.e("SCRAPPER", e.getMessage());
                }
            }
        }).start();




    }
}
