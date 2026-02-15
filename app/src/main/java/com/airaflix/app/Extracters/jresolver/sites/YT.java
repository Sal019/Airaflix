package com.airaflix.app.Extracters.jresolver.sites;

import static com.airaflix.app.Extracters.jresolver.utils.Utils.putModel;

import android.content.Context;
import android.util.SparseArray;

import com.airaflix.app.Extracters.jresolver.Jresolver;
import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.airaflix.app.Extracters.jresolver.utils.Utils;
import com.airaflix.app.Extracters.jresolver.yt.VideoMeta;
import com.airaflix.app.Extracters.jresolver.yt.YouTubeExtractor;
import com.airaflix.app.Extracters.jresolver.yt.YtFile;

import java.util.ArrayList;

public class YT {

    public static void fetch(Context ctx, String url, final Jresolver.OnTaskCompleted onTaskCompleted){
        new YouTubeExtractor(ctx) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    try {
                        ArrayList<Jmodel> jModels = new ArrayList<>();
                        try {
                            int itag = 22;
                            Utils.putModel(ytFiles.get(itag).getUrl(),"Normal",jModels);
                            onTaskCompleted.onTaskCompleted(jModels, false);
                        } catch (Exception A){
                            int itag = 18;
                            Utils.putModel(ytFiles.get(itag).getUrl(),"Normal",jModels);
                            onTaskCompleted.onTaskCompleted(jModels, false);
                        }
                    } catch (Exception Error){
                        onTaskCompleted.onError();
                    }
                } else {
                    onTaskCompleted.onError();
                }
            }
        }.extract(url);
    }
}
