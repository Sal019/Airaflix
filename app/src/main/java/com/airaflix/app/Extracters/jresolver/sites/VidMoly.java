package com.airaflix.app.Extracters.jresolver.sites;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.airaflix.app.Extracters.jresolver.utils.Utils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VidMoly {

    private static String HOST;

    public static void fetch(String url, final Jresolver.OnTaskCompleted onComplete) {

        HOST = Utils.getDomainFromURL(url);

        url = fixURL(url);
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "sources.*file.*\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            AndroidNetworking.get(matcher.group(1))
                                    .addHeaders("Referer", HOST)
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response1) {
                                            //RESOLUTION.*x(.*?),.*[\s\S]http(.*?)[\s\S]8
                                            String regex = "RESOLUTION.*x(.*?),.*[\\s\\S]http(.*?)m3u8";
                                            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                                            Matcher matcher = pattern.matcher(response1);

                                            ArrayList<Jmodel> jModels = new ArrayList<>();
                                            while (matcher.find()) {
                                                Jmodel jModel = new Jmodel();
                                                jModel.setUrl("http"+matcher.group(2)+"m3u8");
                                                jModel.setQuality(matcher.group(1)+"p");
                                                jModels.add(jModel);
                                            }

                                            onComplete.onTaskCompleted(jModels, jModels.size() > 1);
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            anError.printStackTrace();
                                            onComplete.onError();
                                        }
                                    });
                        } else {
                            onComplete.onError();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private static String fixURL(String uri){
        return Utils.getDomainFromURL(uri)+"/embed-"+uri.substring(uri.lastIndexOf("/") + 1)+".html";
    }
}
