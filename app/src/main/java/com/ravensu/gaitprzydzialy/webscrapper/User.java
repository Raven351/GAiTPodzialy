package com.ravensu.gaitprzydzialy.webscrapper;

import java.util.ArrayList;

public class User {
    public String UserName;
    public String Password;
    public ArrayList<Assignment> Assignments;

    public User(String userName, String password) {
        UserName = userName;
        Password = password;
        Assignments = new ArrayList<Assignment>();
    }
}
