package com.ravensu.gaitpodzialy.appdata.sorters;

import com.ravensu.gaitpodzialy.webscrapper.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UsersSorter {
    public static ArrayList<User> sortById (ArrayList<User> users){
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.UserId.compareTo(o2.UserId);
            }
        });
        return users;
    }
}
