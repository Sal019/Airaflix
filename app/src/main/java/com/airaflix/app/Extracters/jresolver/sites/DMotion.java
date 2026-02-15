package com.airaflix.app.Extracters.jresolver.sites;


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

public class DMotion {
    public static void fetch(String url, final Jresolver.OnTaskCompleted onTaskCompleted){

        String meta_url = "https://www.dailymotion.com/player/metadata/video/"+getMediaID(url);

        AndroidNetworking.get(meta_url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("qualities");
                            JSONArray contacts = obj.getJSONArray("auto");
                            ArrayList<Jmodel> models = new ArrayList<>();
                            for (int i = 0; i < contacts.length(); i++) {
                                String url = contacts.getJSONObject(i).getString("url");
                                putModel(url, "Default", models);
                            }

                            if(models.size() > 1){
                                onTaskCompleted.onTaskCompleted(sortMe(models), true);
                            } else {
                                onTaskCompleted.onTaskCompleted(models, false);
                            }

                        } catch (Exception Error){
                            Error.printStackTrace();
                            onTaskCompleted.onError();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static String getMediaID(String url){
        url = url.substring(url.lastIndexOf("/") + 1);
        return url;
    }
}
