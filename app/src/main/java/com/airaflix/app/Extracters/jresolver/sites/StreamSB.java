package com.airaflix.app.Extracters.jresolver.sites;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.airaflix.app.Extracters.jresolver.utils.M3UItem;
import com.airaflix.app.Extracters.jresolver.utils.M3UParser;
import com.airaflix.app.Extracters.jresolver.utils.M3UPlaylist;
import com.airaflix.app.Extracters.jresolver.utils.Utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;

public class StreamSB {

    private static final String UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4136.7 Safari/537.36";
    private static String HOST;
    private static String MEDIAID;
    // --Commented out by Inspection (10/05/2023 10:51):private static String B64DOMAIN;
    public static void fetch(final String url, final Jresolver.OnTaskCompleted onComplete) {

        MEDIAID = Utils.getID(url);
        HOST = Utils.getDomainFromURL(url);
        final String Nurl = generateUrl();
        String ref = "https://sbspeed.com/";

        AndroidNetworking.get(Nurl)
                .setUserAgent(UserAgent)
                .addHeaders("Referer", ref)
                .addHeaders("watchsb", "sbstream")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject obj = response.getJSONObject("stream_data");
                            String file = obj.getString("file");
                            AndroidNetworking.get(file)
                                    .setUserAgent(UserAgent)
                                    .addHeaders("Referer", url)
                                    .addHeaders("watchsb", "streamsb")
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                M3UPlaylist plist = new M3UParser().parseFile(response);
                                                ArrayList<M3UItem> array = plist.getPlaylistItems();
                                                ArrayList<Jmodel> jModels = new ArrayList<>();
                                                for (int i = 0; i < array.size(); i++) {
                                                    M3UItem t = array.get(i);
                                                    Jmodel jModel = new Jmodel();
                                                    jModel.setUrl(t.getItemUrl());
                                                    jModel.setQuality(t.getItemName());
                                                    jModels.add(jModel);
                                                }
                                                onComplete.onTaskCompleted(jModels, jModels.size() > 1);
                                            } catch (FileNotFoundException e) {
                                                onComplete.onError();
                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            onComplete.onError();
                                        }
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });

    }

    private static String generateUrl(){
        String generateLink1 = makeid()+"||"+MEDIAID+"||"+makeid()+"||streamsb"; // x
        String L1binary = asciiTObinary(generateLink1); // c1
        String L1hex = binaryTOhex(L1binary); // c1
        String partial = makeid()+"||"+makeid()+"||"+makeid()+"||streamsb"; // x
        String P1binary = asciiTObinary(partial); // c2
        String P1hex = binaryTOhex(P1binary); // c2
        String LastPart = makeid()+"||"+P1hex+"||"+makeid()+"||streamsb"; // x
        String LastPart1 = asciiTObinary(LastPart);
        String L2hex = binaryTOhex(LastPart1); // c3

        return HOST+"/sources16/"+L1hex+"/"+L2hex;
    }

    private static String makeid() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    private static String asciiTObinary(String s){
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return String.valueOf(binary);
    }

    private static String binaryTOhex(String binary) {
        return new BigInteger(binary, 2).toString(16);
    }

}