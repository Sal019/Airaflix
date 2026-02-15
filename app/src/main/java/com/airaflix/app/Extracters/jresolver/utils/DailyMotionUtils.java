package com.airaflix.app.Extracters.jresolver.utils;

import android.webkit.CookieManager;

import com.airaflix.app.Extracters.jresolver.model.Jmodel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyMotionUtils {
    final ArrayList<Jmodel> jModels = new ArrayList<>();
    OnDone onDone;
// --Commented out by Inspection START (10/05/2023 10:51):
//    @SuppressLint("StaticFieldLeak")
//    public void fetch(String response, OnDone onDone){
//        this.onDone=onDone;
//        try {
//            String json = getJson(response);
//            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(json)).getJSONObject("metadata").getJSONObject("qualities");
//            Iterator<String> iterator = jsonObject.keys();
//            while(iterator.hasNext()) {
//                String key = iterator.next();
//                if (!key.equalsIgnoreCase("auto")) {
//                    JSONArray array = jsonObject.getJSONArray(key);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject temp = array.getJSONObject(i);
//                        String type = temp.getString("type");
//                        String url = temp.getString("url");
//                        if (key.contains("@")) {
//                            key = key.replace("@", "-");
//                        }
//                        String quality = key + "p";
//                        if (type.contains("mp4")) {
//                            putModel(url, quality, jModels);
//                        }
//                    }
//                    showResult();
//                }else {
//                    try {
//                        final String url = jsonObject.getJSONArray(key).getJSONObject(0).getString("url");
//                        new AsyncTask<Void,Void,Void>(){
//                            @Override
//                            protected Void doInBackground(Void... voids) {
//                                try {
//                                    BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
//                                    String line;
//                                    StringBuilder s = new StringBuilder();
//                                    while ((line = reader.readLine()) !=null){
//                                        s.append(line);
//                                    }
//                                    for (String a: s.toString().split("#EXT-X-STREAM-INF")){
//                                        String mUrl = query("PROGRESSIVE-URI",a);
//                                        String mName = query("NAME",a)+"p";
//                                        if (mUrl != null) {
//                                            putModel(mUrl, mName, jModels);
//                                        }
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                return null;
//                            }
//
//                            @Override
//                            protected void onPostExecute(Void aVoid) {
//                                super.onPostExecute(aVoid);
//                                showResult();
//                            }
//                        }.execute();
//                    }catch (Exception e){
//                        showResult();
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            showResult();
//            e.printStackTrace();
//        }
//    }
// --Commented out by Inspection STOP (10/05/2023 10:51)

    private void showResult() {
        if (jModels.size()>0){
            onDone.onResult(jModels);
        }else {
            onDone.onResult(null);
        }
    }

    public interface OnDone{
        void onResult(ArrayList<Jmodel> jModels);
    }

    private String query(String key,String string){
        final String regex = key+"=\"(.*?)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getJson(String html){
        final String regex = "var ?config ?=(.*);";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getDailyMotionID(String link){
        final String regex = "^.+dailymotion.com/(embed)?/?(video|hub)/([^_]+)[^#]*(#video=([^_&]+))?";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            if (matcher.group(5)!=null && !Objects.requireNonNull(matcher.group(5)).equals("null")){
                return removeSlash(Objects.requireNonNull(matcher.group(5)));
            }else return removeSlash(Objects.requireNonNull(matcher.group(3)));
        }
        return null;
    }

    private static void putModel(String url, String quality, ArrayList<Jmodel> model){
        for (Jmodel x:model){
            if (x.getQuality().equalsIgnoreCase(quality)){
                return;
            }
        }
        if (url!=null && quality!=null) {
            Jmodel jModel = new Jmodel();
            jModel.setUrl(url);
            jModel.setQuality(quality);
            String COOKIE = CookieManager.getInstance().getCookie(url);
            if (COOKIE!=null) {
                jModel.setCookie(COOKIE);
            }
            model.add(jModel);
        }
    }

    private static String removeSlash(String ogay){
        if (ogay.contains("/")){
            return ogay.replace("/","");
        }
        return ogay;
    }
}
