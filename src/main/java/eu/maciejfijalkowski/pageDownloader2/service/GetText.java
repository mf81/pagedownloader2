package eu.maciejfijalkowski.pageDownloader2.service;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class GetText {

    public static String get(String url) {
        Document page = null;
        try {
            page = Jsoup.connect(url).get();
            Document doc = Jsoup.parse(page.outerHtml());
            return doc.body().text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String download(String url) {
        try {
            FileUtils.writeStringToFile(new File(domain(url) + ".txt"), get(url));
            return "Done and save in: " + domain(url) + ".txt";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String domain (String url){
        URL domain = null;
        try {
            domain = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return domain.getHost();
    }
}
