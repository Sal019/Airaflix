package com.airaflix.app.Config;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.Key;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class config {

    public static String getpathforsavefile(Context context) {
        return context
                .getSharedPreferences(SETTINGS_NAME, 0)
                .getString(SETTINGS_LIBRARY_DIR, Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public static final int COVER_THUMBNAIL_HEIGHT = 500;
    public static final int COVER_THUMBNAIL_WIDTH = 250;

    public static final int MAX_RECENT_COUNT = 5;

    public static final String SETTINGS_NAME = "SETTINGS_COMICS";
    public static final String SETTINGS_LIBRARY_DIR = "SETTINGS_LIBRARY_DIR";

    public static final String SETTINGS_PAGE_VIEW_MODE = "SETTINGS_PAGE_VIEW_MODE";
    public static final String SETTINGS_READING_LEFT_TO_RIGHT = "SETTINGS_READING_LEFT_TO_RIGHT";

    public static final int MESSAGE_MEDIA_UPDATE_FINISHED = 0;
    public static final int MESSAGE_MEDIA_UPDATED = 1;





}


