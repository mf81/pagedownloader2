package eu.maciejfijalkowski.pageDownloader2.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImgMatcher {

    private Pattern pattern;
    private Pattern pattern2;
    private Matcher matcher;

    private static final String IMAGE_PATTERN = "^(.(?!.*\\.svg$|.*\\.html$|.*\\.css$|.*\\.js$))*$";
    private static final String IMAGE_PATTERN2 = "^(.(.*\\.jpeg$|.*\\.jpg$|.*\\.gif$|.*\\.png$))*$";

    public ImgMatcher(){
        pattern = Pattern.compile(IMAGE_PATTERN);
        pattern2 = Pattern.compile(IMAGE_PATTERN2);
    }

    public boolean validate(final String image){

        matcher = pattern.matcher(image);
        return matcher.matches();

    }

    public boolean validateExtended(final String image){

        matcher = pattern2.matcher(image);
        return matcher.matches();

    }

    public static void main(String[] args) {
        ImgMatcher m = new ImgMatcher();
        System.out.println(m.validateExtended("https://ocdn.eu/weather/weather_state_icons/3.jpg"));
    }

}