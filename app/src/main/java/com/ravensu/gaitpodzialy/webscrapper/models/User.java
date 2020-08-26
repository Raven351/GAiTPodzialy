package com.ravensu.gaitpodzialy.webscrapper.models;

import java.util.ArrayList;

public class User {
    public String UserId;
    public String Password;
    public ArrayList<Assignment> Assignments;
    public ArrayList<Document> Documents;
    public boolean isUserProperlyLoggedIn;

    public User(){}

    public User(String userName, String password) {
        UserId = userName;
        Password = password;
        Assignments = new ArrayList<Assignment>();
        Documents = new ArrayList<Document>();
    }

    public User(String userName, String password, ArrayList<Assignment> assignments) {
        UserId = userName;
        Password = password;
        Assignments = assignments;
    }

    public User(String userName, String password, ArrayList<Assignment> assignments, ArrayList<Document> documents) {
        UserId = userName;
        Password = password;
        Assignments = assignments;
        Documents = documents;
    }

    public User(String userId, String password, boolean isUserProperlyLoggedIn){
        this.UserId = userId;
        this.Password = password;
        this.isUserProperlyLoggedIn = isUserProperlyLoggedIn;
    }

    public User(String userName, String password, ArrayList<Assignment> assignments, ArrayList<Document> documents, boolean isUserProperlyLoggedIn) {
        UserId = userName;
        Password = password;
        Assignments = assignments;
        Documents = documents;
        this.isUserProperlyLoggedIn = isUserProperlyLoggedIn;
    }
}
