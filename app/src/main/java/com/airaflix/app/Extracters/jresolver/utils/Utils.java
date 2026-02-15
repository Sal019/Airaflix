package com.airaflix.app.Extracters.jresolver.utils;

import android.os.StrictMode;
import android.util.Base64;

import com.airaflix.app.Extracters.jresolver.model.Jmodel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static void putModel(String url, String quality, ArrayList<Jmodel> model){
        for (Jmodel x:model){
            if (x.getQuality().equalsIgnoreCase(quality)){
                return;
            }
        }
        if (url!=null && quality!=null) {
            Jmodel jModel = new Jmodel();
            jModel.setUrl(url);
            jModel.setQuality(quality);
            model.add(jModel);
        }
    }

    public static ArrayList<Jmodel> sortMe(ArrayList<Jmodel> x){
        if (x!=null) {
            if (x.size() > 0) {
                ArrayList<Jmodel> result = new ArrayList<>();
                for (Jmodel t : x) {
                    if (startWithNumber(t.getQuality()) || t.getQuality().isEmpty()) {  // with this modificaction it is included those with quality field is empty. EX. openload
                        result.add(t);
                    }
                }
                Collections.sort(result, Collections.reverseOrder());
                return result;
            }
        }
        return null;
    }

    private static boolean startWithNumber(String string){
        final String regex ="^[0-9][A-Za-z0-9-\\s,]*$"; // start with number and can contain space or comma ( 480p , ENG)
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        return  matcher.find();
    }

    public static String getDomainFromURL(String url){
        String regex = "^(?:https?:\\/\\/)?(?:[^@\\n]+@)?(?:www\\.)?([^:\\/\\n?]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    public static String B64Encode(String text){
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT).replace("=","");
    }

    public static String tokenCaptcha(String anchor,String SiteKey,String TheV) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection.Response res = null;
        try {
            res = Jsoup.connect(anchor)
                    .method(Connection.Method.GET)
                    .timeout(15000)
                    .execute();

            Document doc = res.parse();
            String token = doc.getElementById("recaptcha-token").val();
            if (res.statusCode() == 200) {
                Connection.Response res2 = null;
                try {
                    res2 = Jsoup.connect("https://www.google.com/recaptcha/api2/reload?k="+SiteKey)
                            .ignoreContentType(true)
                            .data("v", TheV)
                            .data("reason", "q")
                            .data("c", token)
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .method(Connection.Method.POST)
                            .timeout(15000)
                            .execute();

                    if (res2.statusCode() == 200) {
                        return res2.body().substring(res2.body().indexOf("rresp\",\"") + 8, res2.body().indexOf("\",null"));
                    }
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String GetV(String uri) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection.Response res = null;
        try {
            res = Jsoup.connect(uri)
                    .method(Connection.Method.GET)
                    .timeout(15000)
                    .execute();

            Document doc = res.parse();

            String regex = "releases/([^/]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(doc.toString());
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception Error){
            Error.printStackTrace();
            return  null;
        }

        return null;
    }

    public static String getID(String data){
        if(data.contains(".html")){
            data = data.replace(".html" ,"");
            return data.substring(data.lastIndexOf("/") + 1);
        } else {
            return data.substring(data.lastIndexOf("/") + 1);
        }
    }
}