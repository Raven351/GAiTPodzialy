package com.ravensu.gaitpodzialy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

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
            final SharedPreferences defaultPreferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
            //defaultPreferences.edit().putBoolean("appUsageConditionsAccepted", false).apply(); //for debugging
            if (!defaultPreferences.getBoolean("appUsageConditionsAccepted", false)){
                firstLaunchWarning();
            }
            else loginActivity();
        }
        else{
            assignmentsListActivity();
        }
    }

    private void firstLaunchWarning() {
        final SharedPreferences defaultPreferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.first_launch_welcome_dialog_title)
                .setMessage(R.string.first_launch_welcome_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.first_launch_welcome_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        defaultPreferences.edit().putBoolean("appUsageConditionsAccepted", true).apply();
                        loginActivity();
                    }
                })
                .setNegativeButton(R.string.first_launch_welcome_dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAndRemoveTask();
                    }
                }).show();
    }

    private void loginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    private void assignmentsListActivity(){
        try {
            boolean usersLoaded = UsersData.loadUsersData(this);
            Intent intent = new Intent(this, MainViewPager.class);
            intent.putExtra("FROM_ACTIVITY", "MAIN");
            if (usersLoaded) startActivity(intent); //if there is week connection nul
            else  {
                findViewById(R.id.start_progress_circular).setVisibility(View.INVISIBLE);
                usersLoadingErrorDialog();
            }
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

    private void usersLoadingErrorDialog(){
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(R.string.users_loading_error_dialog_title)
            .setMessage(R.string.users_loading_error_dialog_message)
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            })
            .setNegativeButton(R.string.data_error_dialog_negative_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String url = "http://podzialy.gait.pl/";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    finishAffinity();
                }
            }).show();
    }
}
