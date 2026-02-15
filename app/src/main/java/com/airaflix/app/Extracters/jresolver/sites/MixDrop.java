package com.airaflix.app.Extracters.jresolver.sites;


import static com.airaflix.app.Extracters.jresolver.Jresolver.agent;
import static com.airaflix.app.Extracters.jresolver.utils.Utils.putModel;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.airaflix.app.Extracters.jresolver.utils.JSUnpacker;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MixDrop {

    private static ArrayList<Jmodel> videoModels;

    public static void fetch(String url, final Jresolver.OnTaskCompleted onComplete){

        if(url.contains("/f/") ){
            url = url.replace("/f/", "/e/");
        }else if( url.contains("https://mixdrop.gl") ){
            url = url.replace("https://mixdrop.gl", "https://mixdrop.co");
        }

        AndroidNetworking.get(url)
                .setUserAgent(agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String src = getUrl(response);
                            videoModels = new ArrayList<>();
                            putModel(src,"Normal", videoModels);
                            if (videoModels !=null) {
                                if(videoModels.size() == 0){
                                    onComplete.onError();
                                } else onComplete.onTaskCompleted(videoModels, videoModels.size() > 1);
                            }else onComplete.onError();
                        } catch (Exception A){
                            onComplete.onError();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        onComplete.onError();
                    }
                });

    }

    private static String getUrl(String html){
        Pattern pattern = Pattern.compile("eval\\(function\\(p,a,c,k,e,d\\)(.*?)split");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String jsPacked = "eval(function(p,a,c,k,e,d)"+matcher.group(1)+"split('|'),0,{}))";

            if(jsPacked.contains("MDCore")){
                JSUnpacker jsUnpacker = new JSUnpacker(jsPacked);
                String Unpacked = jsUnpacker.unpack();
                pattern = Pattern.compile("wurl=\"(.*?)\";");
                matcher = pattern.matcher(Unpacked);
                if (matcher.find()) {
                    return "https:"+matcher.group(1);
                }
            }

        }
        return null;
    }
}
