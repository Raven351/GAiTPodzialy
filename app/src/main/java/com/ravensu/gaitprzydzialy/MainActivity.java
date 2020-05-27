package com.ravensu.gaitprzydzialy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ravensu.gaitprzydzialy.activities.data.model.LoggedInUser;
import com.ravensu.gaitprzydzialy.activities.ui.login.LoginActivity;
import com.ravensu.gaitprzydzialy.webscrapper.GAiTWebScrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences appLogins = getSharedPreferences("app_logins", MODE_PRIVATE);
        SharedPreferences.Editor appLoginsEditor = appLogins.edit();
        Map<String, ?> allLogins = appLogins.getAll();
        //appLoginsEditor.putString("3320", "98792501938");
        appLoginsEditor.apply();
        for (Map.Entry<String, ?> entry : allLogins.entrySet()){
            Log.d("LOGINS", entry.getKey() + ":" + entry.getValue().toString());
        }
        if (appLogins.getAll().size() == 0) {
            setContentView(R.layout.activity_login);
        }
        else setContentView(R.layout.activity_main);
        final GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper("", "");

//        boolean firstRun = getSharedPreferences("preferences", MODE_PRIVATE).getBoolean("firstrun", true);
//        if (firstRun){
//            Map<String ,String> loginsMap = new HashMap<>();
//            //ArrayList<LoggedInUser> loggedInUsers = new ArrayList<>();
//            SharedPreferences appLogins = getSharedPreferences("app_logins", MODE_PRIVATE);
//
//        }


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
