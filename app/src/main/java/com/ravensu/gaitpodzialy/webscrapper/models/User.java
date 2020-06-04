package com.ravensu.gaitpodzialy.webscrapper.models;

import java.util.ArrayList;

public class User {
    public String UserId;
    public String Password;
    public ArrayList<Assignment> Assignments;

    public User(){}

    public User(String userName, String password) {
        UserId = userName;
        Password = password;
        Assignments = new ArrayList<Assignment>();
    }

    public User(String userName, String password, ArrayList<Assignment> assignments) {
        UserId = userName;
        Password = password;
        Assignments = assignments;
    }
}
