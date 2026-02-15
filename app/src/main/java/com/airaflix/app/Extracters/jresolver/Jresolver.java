package com.airaflix.app.Extracters.jresolver;

import android.content.Context;

import androidx.annotation.NonNull;

import com.airaflix.app.Extracters.jresolver.model.Jmodel;
import com.airaflix.app.Extracters.jresolver.sites.Archive;
import com.airaflix.app.Extracters.jresolver.sites.DMotion;
import com.airaflix.app.Extracters.jresolver.sites.Dood;
import com.airaflix.app.Extracters.jresolver.sites.HDVid;
import com.airaflix.app.Extracters.jresolver.sites.MFire;
import com.airaflix.app.Extracters.jresolver.sites.MP4Upload;
import com.airaflix.app.Extracters.jresolver.sites.MixDrop;
import com.airaflix.app.Extracters.jresolver.sites.OKRU;
import com.airaflix.app.Extracters.jresolver.sites.StreamHide;
import com.airaflix.app.Extracters.jresolver.sites.StreamLare;
import com.airaflix.app.Extracters.jresolver.sites.StreamSB;
import com.airaflix.app.Extracters.jresolver.sites.StreamTape;
import com.airaflix.app.Extracters.jresolver.sites.StreamVid;
import com.airaflix.app.Extracters.jresolver.sites.Upstream;
import com.airaflix.app.Extracters.jresolver.sites.VK;
import com.airaflix.app.Extracters.jresolver.sites.VidMoly;
import com.airaflix.app.Extracters.jresolver.sites.VideoBM;
import com.airaflix.app.Extracters.jresolver.sites.Vidoza;
import com.airaflix.app.Extracters.jresolver.sites.YT;
import com.airaflix.app.Extracters.jresolver.sites.YodBox;
import com.airaflix.app.Extracters.jresolver.utils.DailyMotionUtils;
import com.androidnetworking.AndroidNetworking;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jresolver {

    private final Context context;
    private OnTaskCompleted onComplete;
    public static final String agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36";
    public static final String doodstream = "(?://|\\.)(do*d(?:stream)?\\.(?:com?|watch|to|s[ho]|cx|la|w[sf]|pm|re|yt))/(?:d|e)/([0-9a-zA-Z]+)";

    public Jresolver(@NonNull Context context){
        this.context=context;
        AndroidNetworking.initialize(context);
    }

    public void find(String url){


        String streamhide = "(?://|\\.)((?:moviesm4u|(?:stream|gucci|mov)hide)\\.(?:to|com|pro))/(?:e|d|w)/([0-9a-zA-Z]+)";
        String streamvid = "(?://|\\.)(streamvid\\.net)/(?:embed-)?([0-9a-zA-Z]+)";
        String streamlare = "(?://|\\.)((?:streamlare|sl(?:maxed|tube|watch))\\.(?:com?|org))/(?:e|v)/([0-9A-Za-z]+)";
        String cloudvideo = ".+(cloudvideo)\\.(tv)/.+";
        String yodbox = "(?://|\\.)(you?dboo?x\\.(?:com|net|org))/(?:embed-)?(\\w+)";
        String upstream = ".+(upstream)\\.(to)/.+";
        String midian = ".+(midian\\.appboxes)\\.(co)/.+";
        String vidmoly = ".+(vidmoly)\\.(me)/.+";
        String eplayvid = ".+(eplayvid)\\.(com|net)/.+";
        String voesx = ".+(voe\\.sx|voe-unblock\\.com).+";
        String hdvid = "(?://|\\.)((?:hdvid|vidhdthe|hdthevid)\\.(?:tv|fun|online|website))/(?:embed-)?([0-9a-zA-Z]+)";
        String googleusercontent = ".+(googleusercontent\\.com).+";
        String deadlyblogger = ".+(deadlyblogger\\.com).+";
        String brighteon = ".+(brighteon\\.com).+";
        String bitchute = ".+(bitchute\\.com)/(?:video|embed).+";
        String archive = ".+(archive)\\.(org)\\/.+";
        String aparat = ".+(aparat\\.com/v/).+";
        String mixdrop = "(?://|\\.)(mixdro?p\\.(?:c[ho]|to|sx|bz|gl|club))/(?:f|e)/(\\w+)";
        String streamsb = "(?://|\\.)((?:view|watch|embed(?:tv)?|tube|player|cloudemb|japopav|javplaya|p1ayerjavseen|gomovizplay|stream(?:ovies)?|vidmovie)?s{0,2}b?(?:embed\\d?|play\\d?|video|fast|full|streams{0,3}|the|speed|l?anh|tvmshow|longvu|arslanrocky|chill|rity|hight|brisk|face|lvturbo|net|one|asian|ani)?\\.(?:com|net|org|one|tv|xyz|fun|pro))/(?:embed[-/]|e/|play/|d/|sup/)?([0-9a-zA-Z]+)";
        String amazon = "https?:\\/\\/(www\\.)?(amazon\\.com)\\/?(clouddrive)\\/+";
        String vudeo = "https?:\\/\\/(www\\.)?(vudeo\\.net)\\/.+";
        String streamtape = "(?://|\\.)(s(?:tr)?(?:eam|have)?(?:ta?p?e?|cloud|adblock(?:plus|er))\\.(?:com|cloud|net|pe|site|link|cc|online|fun|cash|to|xyz))/(?:e|v)/([0-9a-zA-Z]+)";
        String fourShared = "https?:\\/\\/(www\\.)?(4shared\\.com)\\/(video|web\\/embed)\\/.+";
        String vidbm = "(?://|\\.)((?:v[aie]d[bp][aoe]?m|myvii?d|v[aei]{1,2}dshar[er]?)\\.(?:com|net|org|xyz))(?::\\d+)?/(?:embed[/-])?([A-Za-z0-9]+)";
        String vidoza = "(?://|\\.)(vidoza\\.(?:net|co))/(?:embed-)?([0-9a-zA-Z]+)";
        String youtube = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
        String vk = "https?:\\/\\/(www\\.)?vk\\.[^\\/,^\\.]{2,}\\/video\\-.+";
        String okru = "https?:\\/\\/(www.|m.)?(ok)\\.[^\\/,^\\.]{2,}\\/(video|videoembed)\\/.+";
        String mediafire = "https?:\\/\\/(www\\.)?(mediafire)\\.[^\\/,^\\.]{2,}\\/(file)\\/.+";
        String gphoto = "https?:\\/\\/(photos.google.com)\\/(u)?\\/?(\\d)?\\/?(share)\\/.+(key=).+";
        String sendvid = "(?://|\\.)(sendvid\\.com)/(?:embed/)?([0-9a-zA-Z]+)";
        String filerio = "(?://|\\.)(filerio\\.in)/(?:embed-)?([0-9a-zA-Z]+)";
        String mp4upload = "(?://|\\.)(mp4upload\\.com)/(?:embed-)?([0-9a-zA-Z]+)";



        if (check(mp4upload, url)) {
            MP4Upload.fetch(url,onComplete);
        } else if (check(vidmoly,url)){
            VidMoly.fetch(url,onComplete);
        } else if (check(streamhide,url)){
            StreamHide.fetch(url,onComplete);
        } else if (check(streamvid,url)){
            StreamVid.fetch(url,onComplete);
        } else if (check(streamlare,url)){
            StreamLare.fetch(url,onComplete);
        } else if (check(yodbox,url)){
            YodBox.fetch(url,onComplete);
        } else if (check(upstream,url)){
            Upstream.fetch(url,onComplete);
        }  else if (check(hdvid, url)) {
            HDVid.fetch(url,onComplete);
        } else if (check(archive, url)) {
            Archive.fetch(url,onComplete);
        }  else if (check(mixdrop, url)) {
            MixDrop.fetch(url,onComplete);
        } else if (check(streamsb, url)) {
            StreamSB.fetch(url,onComplete);
        } else if (check(doodstream, url)) {
            Dood.fetch(url,onComplete);
        } else if (check(mediafire, url)) {
            MFire.fetch(context,url,onComplete);
        } else if (check(okru,url)){
            OKRU.fetch(url,onComplete);
        } else if (check(vk,url)){
            VK.fetch(url,onComplete);
        } else if (check(vidoza,url)){
            Vidoza.fetch(url,onComplete);
        }  else if (DailyMotionUtils.getDailyMotionID(url)!=null){
            DMotion.fetch(url,onComplete);
        } else if (check(vidbm,url)){
            VideoBM.fetch(url,onComplete);
        }  else if (check(streamtape,url)){
            if(url.contains("shavetape.cash")){
                url = url.replace("shavetape.cash", "streamtape.to");
                StreamTape.fetch(url,onComplete);
            } else {
                StreamTape.fetch(url,onComplete);
            }

        } else if (check(youtube,url)) {
            YT.fetch(context,url, onComplete);
        } else onComplete.onError();
    }
    public void VK(String url){
        VK.fetch(url,onComplete);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<Jmodel> vidURL, boolean multiple_quality);
        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

    private boolean check(String regex, String string) {
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
}
