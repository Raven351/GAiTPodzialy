package com.ravensu.gaitpodzialy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.ravensu.gaitpodzialy.activities.ui.MainViewPager;
import com.ravensu.gaitpodzialy.activities.ui.login.LoginActivity;
import com.ravensu.gaitpodzialy.data.SavedAppLogins;
import com.ravensu.gaitpodzialy.data.SavedAppMainLogin;
import com.ravensu.gaitpodzialy.data.UsersData;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //debug
//        final SharedPreferences appLogins = getSharedPreferences("app_logins", MODE_PRIVATE);
//        SharedPreferences.Editor appLoginsEditor = appLogins.edit();
//        appLoginsEditor.remove(TestingValues.usernameMichu);
//        appLoginsEditor.apply();
//        debug end
        Log.d("MainActivity", "onCreate: "  + SavedAppLogins.GetAllCredentials(this).size());
        Log.d("MainActivity", "onCreate: "  + SavedAppMainLogin.GetMainLoginUserName(this));
        new CountDownTimer(1000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startApp();
            }
        }.start();
    }

    private void startApp(){
        if(!SavedAppLogins.ExistsAny(this)){
            loginActivity();
        }
        else{
            assignmentsListActivity();
        }
    }

    private void loginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    private void assignmentsListActivity(){
        try {
            UsersData.loadUsersData(this);
            Intent intent = new Intent(this, MainViewPager.class);
            intent.putExtra("FROM_ACTIVITY", "MAIN");
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("MAIN", "assignmentsListActivity: Error loading users data" + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                assignmentsListActivity();
            }
        }
    }
}
