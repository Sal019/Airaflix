package com.airaflix.app.Extracters.jresolver.sites;


import static com.airaflix.app.Extracters.jresolver.Jresolver.agent;
import static com.airaflix.app.Extracters.jresolver.utils.Utils.getDomainFromURL;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamTape {

    // --Commented out by Inspection (10/05/2023 10:51):private static final String NAME = "StreamTape";
// --Commented out by Inspection START (10/05/2023 10:51):
//    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:92.0) Gecko/20100101 Firefox/92.0";
//    private static final String PATTERN = "(?://|\\.)(s(?:tr)?(?:eam|have)?(?:ta?p?e?|cloud|adblock(?:plus|er))\\." +
//            "(?:com|cloud|net|pe|site|link|cc|online|fun|cash|to|xyz))/(?:e|v)/([0-9a-zA-Z]+)";
// --Commented out by Inspection STOP (10/05/2023 10:51)

    public static void fetch(String url, final Jresolver.OnTaskCompleted onComplete) {

        if (url.contains("/e/")) {
            url = url.replace("/e/", "/v/");
        }

        if (url.endsWith("mp4")) {
            int lastIndex = url.lastIndexOf('/');
            url = url.substring(0, lastIndex + 1);
        }

        String webUrl = url;
        AndroidNetworking.get(webUrl)
                .addHeaders("User-Agent", agent)
                .addHeaders("Referer", getDomainFromURL(url) + "/")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String content) {
                        Pattern pattern = Pattern.compile("ById\\('.+?=\\s*([\"']//[^;<]+)");
                        Matcher matcher = pattern.matcher(content);
                        if (matcher.find()) {
                            StringBuilder srcUrl = new StringBuilder();

                            String[] parts = Objects.requireNonNull(matcher.group(1)).replace("'", "\"").split("\\+");
                            for (String part : parts) {
                                Pattern p1Pattern = Pattern.compile("\"([^\"]*)");
                                Matcher p1Matcher = p1Pattern.matcher(part);
                                if (p1Matcher.find()) {
                                    String p1 = p1Matcher.group(1);
                                    int p2 = 0;
                                    if (part.contains("substring")) {
                                        Pattern subsPattern = Pattern.compile("substring\\((\\d+)");
                                        Matcher subsMatcher = subsPattern.matcher(part);
                                        while (subsMatcher.find()) {
                                            p2 += Integer.parseInt(Objects.requireNonNull(subsMatcher.group(1)));
                                        }
                                    }
                                    assert p1 != null;
                                    srcUrl.append(p1.substring(p2));
                                }
                            }
                            srcUrl.append("&stream=1");
                            srcUrl = new StringBuilder(srcUrl.toString().startsWith("//") ? "https:" + srcUrl : srcUrl.toString());

                            Jmodel jModel = new Jmodel();
                            jModel.setUrl(srcUrl.toString());
                            jModel.setQuality("Normal");
                            ArrayList<Jmodel> jModels = new ArrayList<>();
                            jModels.add(jModel);
                            onComplete.onTaskCompleted(jModels, false);
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
}
