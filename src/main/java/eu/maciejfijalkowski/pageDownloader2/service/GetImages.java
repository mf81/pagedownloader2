package eu.maciejfijalkowski.pageDownloader2.service;

import org.apache.tika.Tika;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetImages {

    @Autowired
    ImgMatcher imgMatcher;

    public List<String> get(String url) {
        List<String> listOfImages = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();

            Elements img = doc.getElementsByTag("img");

            String fileName=null;

            for (Element el : img) {
                if (!el.absUrl("src").equals("")) {
                    fileName=el.absUrl("src");
                    if(fileName.endsWith("/")) {
                        fileName= fileName.substring(0, fileName.length() - 1);
                    }
                    if(imgMatcher.validate(fileName)){
                        listOfImages.add(fileName);
                    }

                }
            }
            return listOfImages;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String fileName(String url,URL urlImg) {
        InputStream in = null;
        try {
            in = new BufferedInputStream(urlImg.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] partOfPath = url.split("/");

        if (imgMatcher.validateExtended(partOfPath[partOfPath.length-1])) {
            return "./images/" + partOfPath[partOfPath.length - 1];
        }

        return "./images/" + partOfPath[partOfPath.length-1] + "." + fileExtension(in);
    }

    private String fileExtension(InputStream in) {
        Tika tika = new Tika();
        String[] metaData = new String[0];
        try {
            metaData = tika.detect(in).split("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metaData [metaData .length-1];
    }

    public void downloadSingleImg (String url) {

        URL urlImg = null;
        try {
            urlImg = new URL(url);

            String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
            URLConnection con = urlImg.openConnection();
            con.setRequestProperty("User-Agent", USER_AGENT);
            InputStream is = con.getInputStream();

            OutputStream os = new FileOutputStream(fileName(url,urlImg));
            byte[] bitImg = new byte[2048];

            int length;
            while ((length = is.read(bitImg)) != -1) {
                os.write(bitImg, 0, length);
            }

            is.close();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(String url) {
        for(String fileName : get(url)){
            downloadSingleImg(fileName);
        }
    }


}
