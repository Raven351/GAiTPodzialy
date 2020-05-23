package com.ravensu.gaitprzydzialy.webscrapper;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;

public class GAiTWebScrapper {
    private User user;

    public GAiTWebScrapper(User user) {
        this.user = user;
    }

    private Document GetGAiTWebsite(){
        try {
            Connection.Response res = Jsoup.connect("http://podzialy.gait.pl/")
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .timeout(2000)
                    .data("konto", user.UserName)
                    .data("password", user.Password)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = res.parse();
            Log.d("SCRAPPER", "GetGAiTWebsite: " + doc.title());
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SCRAPPER", "GetGAiTWebsite: " + e.toString());
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Assignment> ScrapAssignmentsTable() {
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        Document document =  GetGAiTWebsite();
        Element assignmentsTable = document.select("table").get(1);
        Elements rows = assignmentsTable.select("tr");
        for (int i = 2; i< rows.size(); i++){
            Assignment assignment = new Assignment();
            Elements cols = rows.get(i).select("td");
            if (cols.size() > 0){
                try{
                    assignment.Date = new SimpleDateFormat("yyyy-MM-dd").parse(cols.get(0).text());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assignment.DriverNumber = cols.get(1).text();
                assignment.DriverName = cols.get(2).text();
                assignment.AssignmentCode = cols.get(3).text();
                Log.d("SCRAPPER", "AssignmentCode: " + assignment.AssignmentCode);
                assignment.AssignmentStartLocation = cols.get(4).text();
                assignment.AssignmentStartTime = LocalTime.parse(cols.get(5).text());
                assignment.AssignmentEndTime = LocalTime.parse(cols.get(6).text());
                assignment.AssignmentEndLocation = cols.get(7).text();
                assignment.AssignmentDuration = LocalTime.parse(cols.get(8).text());
                assignment.Comments = cols.get(9).text();
                assignments.add(assignment);
            }
        }
        Log.d("SCRAPPER", "Date: " + assignments.get(0).Date);
        Log.d("SCRAPPER", "Assignments number: " + assignments.size());
        return assignments;
    }
}
