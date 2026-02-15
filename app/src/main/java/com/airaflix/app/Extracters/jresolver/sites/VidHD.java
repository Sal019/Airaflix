package com.airaflix.app.Extracters.jresolver.sites;


import static com.airaflix.app.Extracters.jresolver.utils.Utils.getDomainFromURL;
import static com.airaflix.app.Extracters.jresolver.utils.Utils.getID;

import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.airaflix.app.Extracters.jresolver.utils.JSUnpacker;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VidHD {

// --Commented out by Inspection START (10/05/2023 10:51):
//    public static void fetch(String url, final Jresolver.OnTaskCompleted onComplete){
//
//        url = fixURL(url);
//
//        AndroidNetworking.get(url)
//                .setUserAgent(agent)
//                .build()
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        ArrayList<Jmodel> jModels = parse(response);
//
//                        if(jModels == null){
//                            onComplete.onError();
//                        } else onComplete.onTaskCompleted(jModels, jModels.size() > 1);
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        onComplete.onError();
//                    }
//                });
//
//    }
// --Commented out by Inspection STOP (10/05/2023 10:51)

    private static ArrayList<Jmodel> parse(String response){
        ArrayList<Jmodel> xModels = new ArrayList<>();
        JSUnpacker jsUnpacker = new JSUnpacker(getEvalCode(response));
        if(jsUnpacker.detect()) {
            String src = jsUnpacker.unpack();
            final String regex = "file:\"(.*?)\",.*?\"(.*?)\"";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(src);
            while (matcher.find()) {
                Jmodel xModel = new Jmodel();
                xModel.setUrl(matcher.group(1));
                xModel.setQuality(matcher.group(2));
                xModels.add(xModel);
            }
            return xModels;
        } else {
            return null;
        }
    }

    private static String getEvalCode(String html){
        final String regex = ">eval(.*)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    private static String fixURL(String url){
        if (!url.contains("embed-")) {
            String domain = getDomainFromURL(url);
            String id = getID(url);
            return domain+"/embed-"+id+".html";
        } else {
            return url;
        }
    }
}
