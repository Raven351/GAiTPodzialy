package com.ravensu.gaitprzydzialy.webscrapper;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GAiTWebScrapper {
    private User user = new User("username", "pass");
    public Document GetGAiTWebsite(){
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

}
