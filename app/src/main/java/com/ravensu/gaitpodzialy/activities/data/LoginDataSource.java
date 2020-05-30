package com.ravensu.gaitpodzialy.activities.data;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ravensu.gaitpodzialy.activities.data.model.LoggedInUser;
import com.ravensu.gaitpodzialy.data.AppLogins;
import com.ravensu.gaitpodzialy.data.AppMainLogin;
import com.ravensu.gaitpodzialy.receivers.LoginResultReceiver;
import com.ravensu.gaitpodzialy.webscrapper.GAiTWebScrapper;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private Context context;
    private LoginResultReceiver loginResultReceiver = new LoginResultReceiver(new Handler());
    public LoginDataSource(){

    }

    public LoginDataSource(Context context){
        this.context = context;
    }

    public Result<LoggedInUser> login(final String userId, final String password) {
//        final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, UserLoginService.class);
//        intent.putExtra("receiver", loginResultReceiver);
//        intent.putExtra("username", userId);
//        intent.putExtra("password", password);
//        context.startService(intent);
//
//        try {
//            // TODO: handle loggedInUser authentication
//            Log.d("LoginDataSource", "Attempting to download site" );
//            GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper(userId, password);
//            org.jsoup.nodes.Document gaitWebsite = gAiTWebScrapper.GetGAiTWebsite();
//            if (gaitWebsite == null) throw new Exception("Login failed");
//            Log.d("LoginDataSource", "Site downloaded successfully");
//            Log.d("LoginDataSource", "Saving credidentials to shared preferences");
//            AppLogins.SaveCredidentials(context, userId, password);
//            Log.d("LoginDataSource", "Attempting to scrap assignments table");
//            ArrayList<Assignment> assignments = gAiTWebScrapper.ScrapAssignmentsTable(gaitWebsite);
//
//            String displayName = assignments.get(0).DriverName;
//            LoggedInUser user = new LoggedInUser(userId, displayName, assignments);
//            return new Result.Success<>(user);
//        } catch (Exception e) {
//            Log.e("LoginDataSource", "Error while trying to log in: " + e.toString());
//            return new Result.Error(new IOException("LoginDataSource: Error while trying to log in", e));
//        }

        final Result[] result = new Result[1];
        result[0] = new Result.Error(new IllegalThreadStateException("LoginDataSource: Thread execution failed"));
        Thread loginThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO: handle loggedInUser authentication
                    Log.d("LoginDataSource", "Attempting to download site" );
                    GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper(userId, password);
                    Document gaitWebsite = gAiTWebScrapper.GetGAiTWebsite();
                    if (gaitWebsite == null) throw new Exception("Login failed");
                    Log.d("LoginDataSource", "Site downloaded successfully");
                    Log.d("LoginDataSource", "Saving credidentials to shared preferences");
                    if (!AppLogins.ExistsAny(context)) AppMainLogin.setMainLoginUserId(context, userId);
                    AppLogins.SaveCredidentials(context, userId, password);
                    Log.d("LoginDataSource", "Attempting to scrap assignments table");
                    ArrayList<Assignment> assignments = gAiTWebScrapper.ScrapAssignmentsTable(gaitWebsite);
                    Log.d("LoginDataSource", "Assignments table scrapped successfully");
                    String displayName = assignments.get(0).DriverName;
                    if (!AppLogins.ExistsAny(context)) AppMainLogin.setMainLoginUserName(context, displayName);
                    LoggedInUser user = new LoggedInUser(userId, displayName, assignments);
                    Log.d("LoginDataSource", "Returning ResultSuccess with user model containing data: " + userId + displayName + assignments.size());
                    result[0] = new Result.Success<>(user);
                } catch (Exception e) {
                    Log.e("LoginDataSource", "Error while trying to log in: " + e.toString());
                    result[0] = new Result.Error(new IOException("LoginDataSource: Error while trying to log in", e));
                }
            }
        });
        loginThread.start();
        try {
            loginThread.join(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("LoginDataSource", "Thread interrupted");
            return result[0] = new Result.Error(e);
        }
        return result[0];

    }

    public void logout(String userId) {
        try{
            AppLogins.RemoveCredidentials(context, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public Result<LoggedInUser> onReceiverResult(int resultCode, Bundle resultData) {
//        switch (resultCode){
//            case 0:
//                //todo progress for logging in
//                break;
//            case 1:
//                String userId = resultData.getString("userId");
//                String displayName = resultData.getString("displayName");
//                ArrayList<Assignment> assignments= resultData.getParcelableArrayList("assignments");
//                LoggedInUser user = new LoggedInUser(userId, displayName, assignments);
//                return new Result.Success<>(user);
//            case 2:
//                return new Result.Error(new Exception(resultData.getString(Intent.EXTRA_TEXT)));
//        }
//    }
}
