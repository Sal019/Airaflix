package com.airaflix.app.Extracters.jresolver.model;


import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jmodel implements Comparable<Jmodel>{
    String quality,url,cookie;

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

// --Commented out by Inspection START (10/05/2023 10:51):
//    public String getCookie() {
//        return cookie;
//    }
// --Commented out by Inspection STOP (10/05/2023 10:51)

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


    @NonNull
    @Override
    public String toString() {
        return quality;
    }

    @Override
    public int compareTo(Jmodel jModel) {
        if (startWithNumber(jModel.quality)){
            return Integer.parseInt(quality.replaceAll("\\D+", "")) - Integer.parseInt(jModel.quality.replaceAll("\\D+", ""));
        }
        return this.quality.length() - jModel.quality.length();
    }

    private boolean startWithNumber(String string){
        final String regex ="^\\d[A-Za-z\\d-\\s,]*$";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        return  matcher.find();
    }


}
