package com.ravensu.gaitpodzialy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.ravensu.gaitpodzialy.activities.ui.login.LoginActivity;
import com.ravensu.gaitpodzialy.data.AppLogins;
import com.ravensu.gaitpodzialy.data.AppMainLogin;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //debug
        SharedPreferences appLogins = getSharedPreferences("app_logins", MODE_PRIVATE);
        SharedPreferences.Editor appLoginsEditor = appLogins.edit();
        appLoginsEditor.remove(TestingValues.username);
        appLoginsEditor.apply();
//        debug end
        Log.d("MainActivity", "onCreate: "  + AppLogins.GetAllCredidentials(this).size());
        Log.d("MainActivity", "onCreate: "  + AppMainLogin.GetMainLoginUserName(this));
        if (!AppLogins.ExistsAny(this)) {
            new CountDownTimer(2000, 1000){
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                    loginActivity();
                }
            }.start();
        }
        else Toast.makeText(getApplicationContext(), "Welcome " + AppMainLogin.GetMainLoginUserName(this), Toast.LENGTH_LONG).show();
        //start new intent with tables
    }

    private void loginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, Activity.RESULT_OK);
    }
}
