package com.ravensu.gaitpodzialy.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;

import com.ravensu.gaitpodzialy.data.SavedAppLogins;
import com.ravensu.gaitpodzialy.webscrapper.GAiTWebScrapper;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class UserLoginService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UserLoginService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();
        try {

            receiver.send(0, Bundle.EMPTY);
            GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper(intent.getStringExtra("username"), intent.getStringExtra("password"));
            Document gaitWebsite = gAiTWebScrapper.GetGAiTWebsite();
            if(gaitWebsite == null) throw new Exception("Login failed: GAIT Website Document is null.");
            SavedAppLogins.SaveCredentials(this, intent.getStringExtra("username"), intent.getStringExtra("password"));
            ArrayList<Assignment> assignments = gAiTWebScrapper.ScrapAssignmentsTable(gaitWebsite);
            String displayName = assignments.get(0).DriverName;
            bundle.putString("userId", intent.getStringExtra("username"));
            bundle.putString("displayName", displayName);
            receiver.send(1, bundle);
        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(2,bundle);
        }

    }
}
