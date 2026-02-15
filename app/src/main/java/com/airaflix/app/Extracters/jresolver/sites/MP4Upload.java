package com.airaflix.app.Extracters.jresolver.sites;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.airaflix.app.Extracters.jresolver.utils.JSUnpacker;
import com.airaflix.app.Extracters.jresolver.utils.Utils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MP4Upload {
    public static void fetch(String url, final Jresolver.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(fixURL(url))
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Jmodel> jModels = parseVideo(response);
                        if (jModels==null){
                            onTaskCompleted.onError();
                        }else onTaskCompleted.onTaskCompleted(jModels, false);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static ArrayList<Jmodel> parseVideo(String response){
        JSUnpacker jsUnpacker = new JSUnpacker(getEvalCode(response));
        if(jsUnpacker.detect()) {
            String src = getSrc(jsUnpacker.unpack());
            if (src!=null && src.length()>0){
                ArrayList<Jmodel> jModels = new ArrayList<>();
                Utils.putModel(src,"Normal",jModels);
                if (!jModels.isEmpty()){
                    return jModels;
                }
            }
        }
        return null;
    }

    private static String fixURL(String url){
        if (!url.contains("embed-")) {
            final String regex = "com/([^']*)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String id = matcher.group(1);
                if (Objects.requireNonNull(id).contains("/")) {
                    id = id.substring(0, id.lastIndexOf("/"));
                }
                url = "https://www.mp4upload.com/embed-" + id + ".html";
            }
        }

        return url;
    }

    private static String getSrc(String code){
        final String regex = "src\\(\"(.*?)\"\\);";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String getEvalCode(String html){
        final String regex = "eval\\(function\\(p,a,c,k,e,[rd](.*)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
