package com.airaflix.app.Extracters.jresolver.sites;


import static com.airaflix.app.Extracters.jresolver.utils.Utils.getDomainFromURL;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.airaflix.app.Extracters.jresolver.utils.HttpsTrustManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vidoza {
    public static void fetch( String url, final Jresolver.OnTaskCompleted onTaskCompleted){

        url = fixURL(url);
        if (url!=null) {
            AndroidNetworking.get(url)
                    .setOkHttpClient(HttpsTrustManager.getUnsafeOkHttpClient())
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
                            anError.printStackTrace();
                            onTaskCompleted.onError();
                        }
                    });
        }else onTaskCompleted.onError();
    }

    private static String fixURL(String url){
        if (!url.contains("embed-")) {
            final String regex = "net/([^']*)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String id = matcher.group(1);
                if (Objects.requireNonNull(id).contains("/")) {
                    id = id.substring(0, id.lastIndexOf("/"));
                }
                url = getDomainFromURL(url)+"/embed-" + id;
            } else {
                return null;
            }
        }
        return url;
    }

    private static ArrayList<Jmodel> parse(String response){

        final Pattern pattern = Pattern.compile("src\\s*:\\s*[\"']([^\"']+)", Pattern.MULTILINE);
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
