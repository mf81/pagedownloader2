package eu.maciejfijalkowski.pageDownloader2.controller;

import eu.maciejfijalkowski.pageDownloader2.service.GetImages;
import eu.maciejfijalkowski.pageDownloader2.service.GetText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController{

    private Thread download;

    @Autowired
    GetImages getImages;

    @GetMapping("/api/get/text")
    public String getText(@RequestParam("url") String url){
        return GetText.get(url);
    }

    @GetMapping("/api/download/text")
    public String downloadText(@RequestParam("url") String url){
        return GetText.download(url);
    }

    @GetMapping("/api/get/img")
    public List<String> getImg(@RequestParam("url") String url){
        return getImages.get(url);
    }

    @GetMapping("/api/download/img")
    public String downloadImg(@RequestParam("url") String url){

        download = new Thread(() -> getImages.download(url));
        download.start();

        return "Start downloading";
    }

    @GetMapping("/api/download/check")
    public String downloadCheck(){

        if (download.isAlive())
            return "Downloadnig in progress";
        else
            return "Downloading is DONE !!!";
    }
}
