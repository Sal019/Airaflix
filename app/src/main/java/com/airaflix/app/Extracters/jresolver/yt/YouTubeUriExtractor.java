package com.airaflix.app.Extracters.jresolver.yt;

import android.content.Context;
import android.util.SparseArray;

import java.util.Objects;

@Deprecated
public abstract class YouTubeUriExtractor extends YouTubeExtractor {

    public YouTubeUriExtractor(Context con) {
        super(con);
    }

    @Override
    protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
        onUrisAvailable(Objects.requireNonNull(videoMeta).getVideoId(), videoMeta.getTitle(), ytFiles);
    }

    public abstract void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles);
}