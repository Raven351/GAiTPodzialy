package com.ravensu.gaitpodzialy.activities.data;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ravensu.gaitpodzialy.activities.data.model.LoggedInUser;
import com.ravensu.gaitpodzialy.data.SavedAppLogins;
import com.ravensu.gaitpodzialy.data.SavedAppMainLogin;
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
        final Result[] result = new Result[1];
        result[0] = new Result.Error(new IllegalThreadStateException("LoginDataSource: Thread execution failed"));
        Thread loginThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO: handle loggedInUser authentication
                    GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper(userId, password);
                    Document gaitWebsite = gAiTWebScrapper.GetGAiTWebsite();
                    if (gaitWebsite == null) throw new Exception("Login failed");
                    if (!SavedAppLogins.ExistsAny(context)) SavedAppMainLogin.SetMainLoginUserId(context, userId);
                    ArrayList<Assignment> assignments = gAiTWebScrapper.ScrapAssignmentsTable(gaitWebsite);
                    String displayName = "";
                    if (assignments.size() > 0) displayName = assignments.get(0).DriverName;
                    if (!SavedAppLogins.ExistsAny(context)) SavedAppMainLogin.SetMainLoginUserName(context, displayName);
                    SavedAppLogins.SaveCredentials(context, userId, password);
                    LoggedInUser user = new LoggedInUser(userId, displayName, assignments);
                    result[0] = new Result.Success<>(user);
                } catch (Exception e) {
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
            SavedAppLogins.RemoveCredentials(context, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
