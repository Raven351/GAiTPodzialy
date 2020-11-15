package com.ravensu.gaitpodzialy.appdata;

import com.ravensu.gaitpodzialy.webscrapper.GAiTWebScrapper;
import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;
import com.ravensu.gaitpodzialy.webscrapper.models.User;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ScrapUserDataTask implements Callable<User> {
    private String UserId;
    private String UserPassword;

    public ScrapUserDataTask(String userId, String userPassword){
        this.UserId = userId;
        this.UserPassword = userPassword;
    }

    @Override
    public User call() throws IOException {
        User user = new User(UserId, UserPassword, new ArrayList<Assignment>(), new ArrayList<com.ravensu.gaitpodzialy.webscrapper.models.Document>(), false);
        GAiTWebScrapper gAiTWebScrapper = new GAiTWebScrapper(UserId, UserPassword);
        Document gAiTWebsite = gAiTWebScrapper.GetGAiTWebsite();
        //todo do trycatch for IO Exception that will check if connection was timed out
        if (gAiTWebsite != null){
            ArrayList<Assignment> assignments = gAiTWebScrapper.ScrapAssignmentsTable(gAiTWebsite);
            ArrayList<com.ravensu.gaitpodzialy.webscrapper.models.Document> documents = gAiTWebScrapper.ScrapDocumentsTable(gAiTWebsite);
            user = new User(UserId, UserPassword, assignments, documents, true);
        }
        return user;
    }
}
