package com.airaflix.app.Extracters.jresolver.utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M3UParser {

    // --Commented out by Inspection (10/05/2023 10:51):private static final String EXT_M3U = "#EXTM3U";
  // --Commented out by Inspection (10/05/2023 10:51):  // --Commented out by Inspection (10/05/2023 10:51):private static final String EXT_INF = "#EXT";
// --Commented out by Inspection START (10/05/2023 10:51):
//    private static final String EXT_PLAYLIST_NAME = "#PLAYLIST";
//    private static final String EXT_LOGO = "tvg-logo";
// --Commented out by Inspection STOP (10/05/2023 10:51)
    private static final String EXT_URL = "http";

// --Commented out by Inspection START (10/05/2023 10:51):
//    private String convertStreamToString(InputStream is) {
//        try {
//            return new Scanner(is).useDelimiter("\\A").next();
//        } catch (NoSuchElementException e) {
//            return "";
//        }
//    }
// --Commented out by Inspection STOP (10/05/2023 10:51)

    public M3UPlaylist parseFile(String stream) throws FileNotFoundException {
        stream = stream.replace("#EXTM3U", "");
        stream = stream.replace("\n", "").replace("\r", "");
        stream = stream.replace(" ", "");
        M3UPlaylist m3UPlaylist = new M3UPlaylist();
        ArrayList<M3UItem> playlistItems = new ArrayList<>();
        String[] linesArray = stream.split("#EXT-X");
        for (String currLine : linesArray) {

            M3UItem playlistItem = new M3UItem();

            String[] dataArray = currLine.split(",");
            try {
                String url = dataArray[dataArray.length-1].substring(dataArray[dataArray.length-1].indexOf(EXT_URL));
                Matcher b = Pattern.compile("RESOLUTION.*x(.*?),").matcher(Arrays.toString(dataArray));
                if(b.find()){
                    playlistItem.setItemName(b.group(1)+"p");
                }
                playlistItem.setItemUrl(url);

            } catch (Exception A){
                continue;
            }

                playlistItems.add(playlistItem);

        }
        m3UPlaylist.setPlaylistItems(playlistItems);
        return m3UPlaylist;
    }
}
