package com.ravensu.gaitpodzialy.webscrapper;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.ravensu.gaitpodzialy.webscrapper.models.Assignment;

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
    private String username;
    private String password;

    public GAiTWebScrapper(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     *
     * @return HTML document with GAiT website if logged in properly or null if not.
     */
    public Document GetGAiTWebsite(){
        try {
            Connection.Response res = Jsoup.connect("http://podzialy.gait.pl/")
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .timeout(2000)
                    .data("konto", this.username)
                    .data("password", this.password)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = res.parse();
            Elements docTitle = doc.select("title");
            if(docTitle.isEmpty()) throw new NullPointerException("No <title> tag. Login failed?");
            else {
                Log.d("SCRAPPER", "GetGAiTWebsite - Doc Title: " + doc.title());
                return doc;
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("SCRAPPER", "GetGAiTWebsite: " + e.toString());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SCRAPPER", "GetGAiTWebsite: " + e.toString());
            return null;
        }
    }

    /**
     *
     * @param row HTML <td> cell (column) Elements from single row.
     * @return Single assignment object with assigned values of given elements.
     */
    private Assignment ParseAssignmentRecord(Elements row){
        Assignment assignment = new Assignment();
        try{
            assignment.Date = new SimpleDateFormat("yyyy-MM-dd").parse(row.get(0).text());
            //todo add exception handling for app
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assignment.DriverNumber = row.get(1).text();
        assignment.DriverName = row.get(2).text();
        assignment.AssignmentCode = row.get(3).text();
        Log.d("SCRAPPER", "Służba:" + assignment.AssignmentCode);
        assignment.AssignmentStartLocation = row.get(4).text();
        Log.d("SCRAPPER", "Początek - lokacja " + assignment.AssignmentStartLocation);
        Log.d("SCRAPPER", "===========================");
        assignment.AssignmentStartTime = LocalTime.parse(row.get(5).text());
        assignment.AssignmentEndTime = LocalTime.parse(row.get(6).text());
        assignment.AssignmentEndLocation = row.get(7).text();
        assignment.AssignmentDuration = LocalTime.parse(row.get(8).text());
        assignment.Comments = row.get(9).text();
        return assignment;
    }

    private com.ravensu.gaitpodzialy.webscrapper.models.Document ParseDocumentRecord(Elements row){
        com.ravensu.gaitpodzialy.webscrapper.models.Document document = new com.ravensu.gaitpodzialy.webscrapper.models.Document();
        try{
            document.Date = new SimpleDateFormat("yyyy-MM-dd").parse(row.get(0).text());
        } catch (ParseException e){
            e.printStackTrace();
        }
        document.Name = row.get(1).text();
        document.Number = row.get(2).text();
        document.URL = row.select("a").attr("href");
        return document;
    }

    /**
     *
     * @param assignmentsTable HTML <table> element containing <tr> rows elements with <td> cells.
     * @return ArrayList of Assignment objects with assigned values of given table.
     */
    private ArrayList<Assignment> ParseAssignmentsToArrayList(Element assignmentsTable){
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        Elements rows = assignmentsTable.select("tr");
        //todo fix case when assignments null (login impossible)
        if (rows.size() < 3) return  assignments;
        for (int i = 2; i< rows.size(); i++){
            Elements row = rows.get(i).select("td");
            if (row.size() > 0){
                Assignment assignment = ParseAssignmentRecord(row);
                assignments.add(assignment);
            }
        }
        return assignments;
    }

    private ArrayList<com.ravensu.gaitpodzialy.webscrapper.models.Document> ParseDocumentsToArrayList(Element documentsTable){
        ArrayList<com.ravensu.gaitpodzialy.webscrapper.models.Document> documents = new ArrayList<com.ravensu.gaitpodzialy.webscrapper.models.Document>();
        Elements rows = documentsTable.select("tr");
        if (rows.size() < 3) return documents;
        for (int i = 2; i< rows.size(); i++){
            Elements row = rows.get(i).select("td");
            if (row.size() > 0){
                com.ravensu.gaitpodzialy.webscrapper.models.Document document = ParseDocumentRecord(row);
                documents.add(document);
            }
        }
        return documents;
    }

    /**
     * @param GAiTWebsite HTML Document of GAiTWebsite.
     * @return ArrayList of Assignemnt objects with assigned data from the assignment table of given HTML document.
     * @throws NullPointerException if given HTML document is null.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Assignment> ScrapAssignmentsTable(Document GAiTWebsite) {
        Document document =  GAiTWebsite;
        if (document == null) throw new NullPointerException("ScrapAssignmentsTable: Given HTML document is null");
        ArrayList<Assignment> assignments = ParseAssignmentsToArrayList(document.select("table").get(1));
        Log.d("SCRAPPER", "Assignments count: " + assignments.size());
        return assignments;
    }

    public ArrayList<com.ravensu.gaitpodzialy.webscrapper.models.Document> ScrapDocumentsTable(Document GAiTWebsite){
        Document document = GAiTWebsite;
        if (document == null) throw new NullPointerException("ScrapDocumentsTable: Given HTML document is null");
        ArrayList<com.ravensu.gaitpodzialy.webscrapper.models.Document> documents = ParseDocumentsToArrayList(document.select("table").get(3));
        return documents;
    }
}
