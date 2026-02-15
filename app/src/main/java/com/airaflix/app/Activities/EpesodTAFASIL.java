package com.airaflix.app.Activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.airaflix.app.Activities.MyList.Mylist.changestatucolor;
import static com.airaflix.app.Activities.Settings.SettingsJcartoon.SETTINGS_NAME;
import static com.airaflix.app.Config.config.getpathforsavefile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airaflix.app.Activities.Auth.LoginTojcartoon;
import com.airaflix.app.Activities.Jdownload.DownloadManage;
import com.airaflix.app.Config.Function;
import com.airaflix.app.Config.getComicsnextback;
import com.airaflix.app.DB.JseriesDB;
import com.airaflix.app.JPLAYER.JSPLAYER;
import com.airaflix.app.Models.EpesodModel;
import com.airaflix.app.Models.SeasonModel;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.Models.ServerModel;
import com.airaflix.app.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.inside4ndroid.jresolver.Jresolver;
import com.inside4ndroid.jresolver.Model.Jmodel;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EpesodTAFASIL extends AppCompatActivity {

    public static final String SECURE_URI = "secure_uri";
    public static final String USER_AGENT = "User-Agent";
    public static final String VIDEOTYPE = "video/*";
    public static final String TITLE = "title";
    public static final String POSTER = "poster";
    public static final String EXTRA_HEADERS = "android.media.intent.extra.HTTP_HEADERS";
    public static final String HEADERS = "headers";
    public static final String REFER = "Referer";

    ImageView backch,fhddwon,hddwon,sddwon,qddwon,coment,next,backarr;
    TextView namechrabar;
    LinearLayout fhdtitle,hdtitle,sdtitle,qdtitle;
    RelativeLayout reportnotepe;
    Boolean IsInMyFavorit;
    private int progressValue = 0;
    public StartAppAd startAppAd;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //LowCostVideo xGetter;
    String title;
    String name,CartoonTitle,Epesodetitle,hdlink,postkey,epetitle,type,fhdlink,sdlink,qdlink,size;
    RewardedAd mRewardedAd;

    Jresolver jresolver;
    int positiondn;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    JseriesDB comicsdb;
    SerieModel tvmodel;
    SeasonModel seasonModel;
    ServerModel serverModel;

    private boolean checkPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final List<String> listPermissionsNeeded = new ArrayList<>();
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1000);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1000){
            if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //downloadFile(current_Xmodel);
            } else {
                checkPermissions();
                Toast.makeText(this, "عليك أن تسمح للتطبيق أولا ", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @SuppressLint("MissingInflatedId")
    public void done2(String xModel){
        String url = null;
        if (xModel!=null) {
            url = xModel;

        }


        if (url!=null) {

            String finalUrl = url;
            Dialog quality_selector = new Dialog(EpesodTAFASIL.this);
            quality_selector.setCancelable(true);
            quality_selector.setContentView(R.layout.quality_selector_instagram);



            LinearLayout qualities = quality_selector.findViewById(R.id.quality_list);

            LayoutInflater inflater = LayoutInflater.from(EpesodTAFASIL.this);

            for (int i = 0; i <  1; i++) {

                final int fi = i;

                View quality_bar = inflater.inflate(R.layout.quality_selector_epesode_item, null);


                ImageView play = quality_bar.findViewById(R.id.playit);
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       switch (sp.getInt("playerselect",0)){

                           case 0:
                               watchVideo2(finalUrl);
                               break;
                           case 1:
                               openWithMXPlayer2(finalUrl);
                               break;
                           case 2:
                               openOtherPlayers(finalUrl);
                               break;
                       }

                        quality_selector.dismiss();

                    }
                });

                View download = quality_bar.findViewById(R.id.downloadit);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(EpesodTAFASIL.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            String fulltitle = CartoonTitle +" - "+ epetitle;
                            switch (sp.getInt("downcartoonselect",0)){

                                case 0:

                                    SingleDownloadepe(finalUrl,CartoonTitle,Epesodetitle,getApplicationContext());

                                    break;
                                case 1:
                                    downloadFromAdm(getApplicationContext(),finalUrl,fulltitle);

                                    break;
                            }
                            quality_selector.dismiss();
                        }
                        else {

                            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }
                });

                qualities.addView(quality_bar);
            }


            quality_selector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            quality_selector.show();
        }else {
            Dialog quality_selector = new Dialog(EpesodTAFASIL.this);
            quality_selector.setCancelable(true);
            quality_selector.setContentView(R.layout.nolinkswork);
            quality_selector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            quality_selector.show();
        }

    }

    private void openOtherPlayers(String finalUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(finalUrl), "video/*");
        startActivity(intent);
    }
    private void SingleDownloadepe(String epelink, String title, String epetitle, Context context) {

        String pathsaved = getpathforsavefile(EpesodTAFASIL.this);

        File directory = new File(pathsaved +"/.JSERIES");
        File comics = new File(directory.getPath()+"/SERIES/");
        File ComicsFolder = new File(comics.getPath()+"/"+title+"/");
        File ComicsIssueFolder = new File(ComicsFolder.getPath()+"/"+epetitle+".mp4");

        // File chapter = new File(ComicsIssueFolder.getPath()+"/"+ position+1 + ".rar");


        DownloadManage.getInstance(context)
                .addDownloadRequest(epelink,ComicsIssueFolder,title,epetitle);
    }

    public static void downloadFromAdm(Context context, String url, String s) {

        Intent shareVideo = new Intent(Intent.ACTION_VIEW);
        shareVideo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareVideo.setDataAndType(Uri.parse(url), VIDEOTYPE);
        shareVideo.setPackage("com.dv.adm");
        ComponentName name = new ComponentName("com.dv.adm","com.dv.get.AEditor");
        shareVideo.setComponent(name);
        shareVideo.putExtra("com.android.extra.filename",s);

        Bundle headers = new Bundle();
        shareVideo.putExtra(SECURE_URI, true);
        try {
            context.startActivity(shareVideo);
        } catch (ActivityNotFoundException ex) {
            // Open Play Store if it fails to launch the app because the package doesn't exist.
            // Alternatively you could use PackageManager.getLaunchIntentForPackage() and check for null.
            // You could try catch this and launch the Play Store website if it fails but this shouldn’t
            // fail unless the Play Store is missing.

            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dv.adm")));
            } catch (ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dv.adm")));
            }

        }
    }

    public boolean appInstalledOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public  void openWithMXPlayer2(String url) {
        boolean appInstalledOrNot = appInstalledOrNot("com.mxtech.videoplayer.ad");
        boolean appInstalledOrNot2 = appInstalledOrNot("com.mxtech.videoplayer.pro");
        String str2;
        if (appInstalledOrNot || appInstalledOrNot2) {
            String str3;
            if (appInstalledOrNot2) {
                str2 = "com.mxtech.videoplayer.pro";
                str3 = "com.mxtech.videoplayer.ActivityScreen";
            } else {
                str2 = "com.mxtech.videoplayer.ad";
                str3 = "com.mxtech.videoplayer.ad.ActivityScreen";
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "application/x-mpegURL");
                intent.setPackage(str2);
                intent.setClassName(str2, str3);
                startActivity(intent);
                return;
            } catch (Exception e) {
                e.fillInStackTrace();
                Log.d("errorMx", e.getMessage());
                return;
            }
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mxtech.videoplayer.ad")));
        } catch (ActivityNotFoundException e2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mxtech.videoplayer.ad")));
        }
    }

    private void watchVideo2(String url){

        Intent sendDataToDetailsActivity = new  Intent(this, JSPLAYER.class);
        sendDataToDetailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        sendDataToDetailsActivity.putExtra("tvmodel",tvmodel);
        sendDataToDetailsActivity.putExtra("seasonmodel",seasonModel);
        sendDataToDetailsActivity.putExtra("episodesmodel", serverModel);
        sendDataToDetailsActivity.putExtra("message","");




        startActivity(sendDataToDetailsActivity);

    }


    private void letGo2(String url)                 {
        String uss = url;
        LoginTojcartoon.PleaseWait.show(EpesodTAFASIL.this);



        if (url.contains("https://fembed.com/f/") ){
            uss = url.replace("https://fembed.com/f/","https://fembed.com/v/");
            jresolver.find(uss);
        }else if (url.contains("https://www.fembed.com/f/")){
            uss = url.replace("https://www.fembed.com/f/","https://fembed.com/v/");
            jresolver.find(uss);

        }else  {
            jresolver.find(url);
        }






    }
    private SharedPreferences sp;

    public String positionEpes0de;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changestatucolor(this);
        setContentView(R.layout.activity_epesod_t_a_f_a_s_i_l);

        StartAppAd.init(this,"107796986","210473406");
        startAppAd = new StartAppAd(this);
        startAppAd.loadAd(StartAppAd.AdMode.VIDEO);

        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        comicsdb = new JseriesDB(EpesodTAFASIL.this);

       InitialisData();


        jresolver = new Jresolver(this);
        jresolver.onFinish(new Jresolver.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<Jmodel> vidURL, boolean multiple_quality) {
                LoginTojcartoon.PleaseWait.dismiss();
                if (multiple_quality){
                    if (vidURL!=null) {
                        multipleQualityDialog2(vidURL);
                    }else done2(null);
                }else {
                    done2(vidURL.get(0).getUrl());
                }
            }

            @Override
            public void onError() {
                LoginTojcartoon.PleaseWait.dismiss();
                done2(null);
            }
        });




        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        //ADS





        backch = findViewById(R.id.backch);
        fhddwon = findViewById(R.id.fhddwon);
        hddwon = findViewById(R.id.hddwon);
        sddwon = findViewById(R.id.sddwon);
        qddwon = findViewById(R.id.qddwon);
        namechrabar = findViewById(R.id.namechrabar);
        fhdtitle = findViewById(R.id.fhdd);
        hdtitle = findViewById(R.id.hdd);
        sdtitle = findViewById(R.id.sdd);
        qdtitle = findViewById(R.id.qdd);
        next = findViewById(R.id.next);
        backarr = findViewById(R.id.backarr);
        reportnotepe = findViewById(R.id.reportnotepe);

        //get the Intents
        epetitle = getIntent().getStringExtra("epetitle");
        positionEpes0de  = getIntent().getStringExtra("position");

        type = getIntent().getStringExtra("type");
        fhdlink = getIntent().getStringExtra("fhd");
        hdlink = getIntent().getStringExtra("hd");
        sdlink = getIntent().getStringExtra("sd");
        qdlink = getIntent().getStringExtra("qd");
        postkey = getIntent().getStringExtra("key");
        String globalname = getIntent().getStringExtra("globalname");
        name = getIntent().getStringExtra("name");

        CartoonTitle = getIntent().getStringExtra("title");

        positiondn = Function.ToInterger(positionEpes0de);


        SetEpeLinks(getComicsnextback.Epesodes.get(positiondn));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (sp.getString("ispaid","0").equals("1")){
                        positiondn++;
                        SetEpeLinks(getComicsnextback.Epesodes.get(positiondn));
                    }else {
                        positiondn++;
                        SetEpeLinks(getComicsnextback.Epesodes.get(positiondn));
                        startAppAd.showAd();
                    }


            }
        });

        backarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sp.getString("ispaid","0").equals("1")){
                    positiondn--;
                    SetEpeLinks(getComicsnextback.Epesodes.get(positiondn));
                }else {
                    positiondn--;
                    SetEpeLinks(getComicsnextback.Epesodes.get(positiondn));
                    startAppAd.showAd();
                }




            }
        });


        backch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });








    }

    private void InitialisData() {

        Bundle extras = getIntent().getExtras();

        tvmodel = extras.getParcelable("tvmodel");
        seasonModel = extras.getParcelable("seasonmodel");
        serverModel = extras.getParcelable("episodesmodel");




    }

    public static void SendReport(String text, String postkey) {

        DatabaseReference report = FirebaseDatabase.getInstance().getReference("reports").child("epesodes");
        HashMap<String,String> epereport = new HashMap<>();
        epereport.put("problemID",text);
        epereport.put("nameofEpesode",postkey);
        report.push().setValue("epereport");


    }
    private void SetEpeLinks(ServerModel epesodModel) {
        Epesodetitle = epesodModel.getLebel();
        if (positiondn == getComicsnextback.Epesodes.size()-1 ){
            next.setVisibility(View.INVISIBLE);
        }else {
            next.setVisibility(View.VISIBLE);

        }
        if (positiondn == 0){
            backarr.setVisibility(View.INVISIBLE);


        }else {
            backarr.setVisibility(View.VISIBLE);

        }

        namechrabar.setText(epesodModel.getLebel());

        reportnotepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendReport(postkey,name + " - " + epesodModel.getLebel());

            }
        });



        fhdtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                letGo2(epesodModel.getUrl());




            }
        });
        hdtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                letGo2(epesodModel.getUrl());

            }
        });
        sdtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                letGo2(epesodModel.getUrl());

            }
        });

    }

    private void OpenDialog(String server2) {

        Dialog mBuilder = new Dialog(EpesodTAFASIL.this);
        mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mBuilder.setContentView(R.layout.custom_dialog_layout);
        mBuilder.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mBuilder.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        ImageView bt_close = mBuilder.findViewById(R.id.bt_close);
        RelativeLayout goplans1 = mBuilder.findViewById(R.id.goplans1);
        RelativeLayout watchfree = mBuilder.findViewById(R.id.watchfree);




        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBuilder.dismiss();
            }
        });

        goplans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EpesodTAFASIL.this, Premium.class));
                mBuilder.dismiss();


            }
        });
        watchfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startAppAd.showAd(new AdDisplayListener() {
                    @Override
                    public void adHidden(Ad ad) {
                        letGo2(server2);

                    }

                    @Override
                    public void adDisplayed(Ad ad) {

                    }

                    @Override
                    public void adClicked(Ad ad) {

                    }

                    @Override
                    public void adNotDisplayed(Ad ad) {
                        letGo2(server2);
                    }
                });
                mBuilder.dismiss();

            }
        });




        mBuilder.show();
        mBuilder.getWindow().setAttributes(lp);
    }



    private void multipleQualityDialog2(ArrayList<Jmodel> model) {

        int player = sp.getInt("playerselect",0);


        Dialog quality_selector = new Dialog(EpesodTAFASIL.this);
        quality_selector.setContentView(R.layout.quality_selector_instagram);
        quality_selector.setCancelable(true);


        LinearLayout qualities = quality_selector.findViewById(R.id.quality_list);

        LayoutInflater inflater = LayoutInflater.from(EpesodTAFASIL.this);

        for (int i = 0; i <  model.size(); i++) {

            final int fi = i;

            View quality_bar = inflater.inflate(R.layout.quality_selector_epesode_item, null);

            TextView q = quality_bar.findViewById(R.id.quality);
            q.setText(model.get(i).getQuality());


            ImageView playit = quality_bar.findViewById(R.id.playit);
            playit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player == 0){
                        watchVideo2(model.get(fi).getUrl());
                    }else if (player == 1){
                        openWithMXPlayer2(model.get(fi).getUrl());
                    }else {
                        openOtherPlayers(model.get(fi).getUrl());
                    }
                    quality_selector.dismiss();
                }
            });


            ImageView downloadit = quality_bar.findViewById(R.id.downloadit);
            downloadit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(EpesodTAFASIL.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        String fulltitle = title +" - "+ Epesodetitle+".mp4";

                        switch (sp.getInt("downcartoonselect",0)){

                            case 0:

                                SingleDownloadepe(model.get(fi).getUrl(),title,Epesodetitle,EpesodTAFASIL.this);

                                break;
                            case 1:

                                downloadFromAdm(EpesodTAFASIL.this,model.get(fi).getUrl(),fulltitle);

                                break;
                        }
                        quality_selector.dismiss();
                    }
                    else {

                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
            });





            qualities.addView(quality_bar);
        }


        quality_selector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quality_selector.show();
    }



}