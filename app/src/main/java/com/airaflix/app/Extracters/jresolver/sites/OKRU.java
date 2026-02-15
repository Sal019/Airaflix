package com.airaflix.app.Extracters.jresolver.sites;


import static com.airaflix.app.Extracters.jresolver.Jresolver.agent;
import static com.airaflix.app.Extracters.jresolver.utils.Utils.putModel;
import static com.airaflix.app.Extracters.jresolver.utils.Utils.sortMe;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OKRU {
    public static void fetch(String url, final Jresolver.OnTaskCompleted onComplete){

        String metaUri = "http://www.ok.ru/dk";

        AndroidNetworking.post(metaUri)
                .addHeaders("User-agent", agent)
                .addBodyParameter("cmd", "videoPlayerMetadata")
                .addBodyParameter("mid", getMediaID(url))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray contacts = response.getJSONArray("videos");
                            ArrayList<Jmodel> models = new ArrayList<>();
                            for (int i = 0; i < contacts.length(); i++) {
                                String url = contacts.getJSONObject(i).getString("url");
                                String name = contacts.getJSONObject(i).getString("name");
                                switch (name) {
                                    case "mobile":
                                        putModel(url, "144p", models);
                                        break;
                                    case "lowest":
                                        putModel(url, "240p", models);
                                        break;
                                    case "low":
                                        putModel(url, "360p", models);
                                        break;
                                    case "sd":
                                        putModel(url, "480p", models);
                                        break;
                                    case "hd":
                                        putModel(url, "720p", models);
                                        break;
                                    case "full":
                                        putModel(url, "1080p", models);
                                        break;
                                    case "quad":
                                        putModel(url, "2000p", models);
                                        break;
                                    case "ultra":
                                        putModel(url, "4000p", models);
                                        break;
                                    default:
                                        putModel(url, "Default", models);
                                        break;
                                }
                            }
                            onComplete.onTaskCompleted(sortMe(models), true);
                        } catch (Exception Error){
                            onComplete.onError();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private static String getMediaID(String url){
        url = url.substring(url.lastIndexOf("/") + 1);
        return url;
    }
}
