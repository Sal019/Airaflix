package com.airaflix.app.Extracters.jresolver.sites;

import android.annotation.SuppressLint;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoBM {

    @SuppressLint("JavascriptInterface")
    public static void fetch(String url, final Jresolver.OnTaskCompleted onTaskCompleted){

        if (url!=null) {
            AndroidNetworking.get(url)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<Jmodel> jModels = parse(response);
                            if (jModels!=null) {
                                onTaskCompleted.onTaskCompleted(jModels, false);
                            }else onTaskCompleted.onError();
                        }

                        @Override
                        public void onError(ANError anError) {
                            onTaskCompleted.onError();
                        }
                    });
        }else onTaskCompleted.onError();
    }

    private static ArrayList<Jmodel> parse(String response){

        final Pattern pattern = Pattern.compile("sources.*file.*?\"(.*?)\",", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            Jmodel jModel = new Jmodel();
            jModel.setUrl(matcher.group(1));
            jModel.setQuality("Normal");
            ArrayList<Jmodel> jModels = new ArrayList<>();
            jModels.add(jModel);
            return jModels;
        }

        return null;
    }
}
