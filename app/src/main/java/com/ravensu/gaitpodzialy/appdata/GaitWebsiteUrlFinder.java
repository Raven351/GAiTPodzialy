package com.ravensu.gaitpodzialy.appdata;

public class GaitWebsiteUrlFinder {
    private final String UserId;
    private final String PODZIALY = "http://podzialy.gait.pl/";
    private final String TRAM = "http://tram.gait.pl/";

    public GaitWebsiteUrlFinder(String userId) {
        UserId = userId;
    }

    public String getSiteUrl(){
        char firstLetterUsername = UserId.charAt(0);
        if (firstLetterUsername == '3') return TRAM;
        else return PODZIALY;
    }
}
