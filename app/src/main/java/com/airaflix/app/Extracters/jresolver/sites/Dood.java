package com.airaflix.app.Extracters.jresolver.sites;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dood {

    static String urlt;
    static String token;
    static boolean isLong;
    public static void fetch(String url, final Jresolver.OnTaskCompleted onComplete) {
        String ref = url;

        if(url.contains("doodstream.com")){
            url = url.replace("doodstream.com", "dood.yt");
        }

        if(url.contains("LONG")){
            isLong = true;
            url = url.replace("LONG", "");
        } else {
            isLong = false;
            url = url.replace("/d/", "/e/");
        }

        final String finalUrl = url;
        String finalUrl1 = url;
        AndroidNetworking.get(url)
                .addHeaders("Referer", ref)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (isLong) {
                            Pattern pattern = Pattern.compile("<iframe src=\"(.*?)\"");
                            Matcher matcher = pattern.matcher(response);
                            if(matcher.find()){

                                try {
                                    AndroidNetworking.get("https://" + getHost(matcher.group(1)))
                                            .build()
                                            .getAsString(new StringRequestListener() {
                                                @Override
                                                public void onResponse(String response) {

                                                    Pattern pattern = Pattern.compile("dsplayer\\.hotkeys[^']+'([^']+).+?function");
                                                    Matcher matcher = pattern.matcher(response);
                                                    if (matcher.find()) {
                                                        try {
                                                            urlt = "https://" + getHost(finalUrl) + matcher.group(1);
                                                        } catch (MalformedURLException a) {
                                                            a.printStackTrace();
                                                        }

                                                        pattern = Pattern.compile("makePlay.+?return[^?]+([^\"]+)");
                                                        matcher = pattern.matcher(response);
                                                        if (matcher.find()) {
                                                            token = matcher.group(1);

                                                            AndroidNetworking.get(urlt)
                                                                    .addHeaders("Referer", finalUrl)
                                                                    .build()
                                                                    .getAsString(new StringRequestListener() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            String test = response + randomStr() + token + System.currentTimeMillis() / 1000L;
                                                                            Jmodel jModel = new Jmodel();
                                                                            jModel.setUrl(test);
                                                                            jModel.setQuality("Normal");
                                                                            ArrayList<Jmodel> jModels = new ArrayList<>();
                                                                            jModels.add(jModel);
                                                                            onComplete.onTaskCompleted(jModels, false);
                                                                        }

                                                                        @Override
                                                                        public void onError(ANError anError) {
                                                                            onComplete.onError();
                                                                        }
                                                                    });
                                                        }

                                                    }



                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                    onComplete.onError();
                                                }
                                            });
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }


                        } else {
                            Pattern pattern = Pattern.compile("dsplayer\\.hotkeys[^']+'([^']+).+?function");
                            Matcher matcher = pattern.matcher(response);
                            if (matcher.find()) {
                                try {
                                    urlt = "https://" + getHost(finalUrl) + matcher.group(1);
                                } catch (MalformedURLException a) {
                                    a.printStackTrace();
                                }

                                pattern = Pattern.compile("makePlay.+?return[^?]+([^\"]+)");
                                matcher = pattern.matcher(response);
                                if (matcher.find()) {
                                    token = matcher.group(1);

                                    AndroidNetworking.get(urlt)
                                            .addHeaders("Referer", finalUrl)
                                            .build()
                                            .getAsString(new StringRequestListener() {
                                                @Override
                                                public void onResponse(String response) {
                                                    String test = response + randomStr() + token + System.currentTimeMillis() / 1000L;

                                                    Jmodel jModel = new Jmodel();
                                                    jModel.setUrl(test);
                                                    jModel.setQuality("Normal");
                                                    ArrayList<Jmodel> jModels = new ArrayList<>();
                                                    jModels.add(jModel);
                                                    onComplete.onTaskCompleted(jModels, false);
                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                    onComplete.onError();
                                                }
                                            });
                                }

                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private static String randomStr() {
        final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rnd = new SecureRandom();


        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();

    }

    private static String getHost(String uri) throws MalformedURLException {
        URL url = new URL(uri);

        return url.getHost();
    }
}
