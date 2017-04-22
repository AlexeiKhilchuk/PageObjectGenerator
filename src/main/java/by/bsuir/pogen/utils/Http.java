package by.bsuir.pogen.utils;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class Http {
    public static boolean isPageExist(String url){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
