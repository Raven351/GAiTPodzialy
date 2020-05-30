package com.ravensu.gaitpodzialy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        //debug end
        if (!AppLogins.ExistsAny(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Activity.RESULT_OK);
        }
        Toast.makeText(getApplicationContext(), "Welcome " + AppMainLogin.getMainLoginUserName(this), Toast.LENGTH_LONG).show();
        //start new intent with tables
    }
}
