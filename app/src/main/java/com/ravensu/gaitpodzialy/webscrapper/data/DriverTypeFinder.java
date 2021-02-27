package com.ravensu.gaitpodzialy.webscrapper.data;

import com.ravensu.gaitpodzialy.webscrapper.enums.DriverType;

public class DriverTypeFinder {
    private final String UserId;

    public DriverTypeFinder(String userId) {
        UserId = userId;
    }

    public DriverType getDriverType(){
        char firstLetterUsername = UserId.charAt(0);
        if (firstLetterUsername == '3') return DriverType.TRAM;
        else return DriverType.BUS;
    }
}
