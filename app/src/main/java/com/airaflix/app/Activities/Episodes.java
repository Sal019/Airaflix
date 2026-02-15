package com.airaflix.app.Activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.airaflix.app.Activities.EpesodTAFASIL.EXTRA_HEADERS;
import static com.airaflix.app.Activities.EpesodTAFASIL.HEADERS;
import static com.airaflix.app.Activities.EpesodTAFASIL.POSTER;
import static com.airaflix.app.Activities.EpesodTAFASIL.SECURE_URI;
import static com.airaflix.app.Activities.EpesodTAFASIL.TITLE;
import static com.airaflix.app.Activities.EpesodTAFASIL.USER_AGENT;
import static com.airaflix.app.Activities.EpesodTAFASIL.VIDEOTYPE;
import static com.airaflix.app.Activities.MySeasons.VLC_INTENT;
import static com.airaflix.app.Activities.MySeasons.VLC_PACKAGE_NAME;
import static com.airaflix.app.Activities.ShowInfoActivity.CheckAds;
import static com.airaflix.app.Config.config.SETTINGS_NAME;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airaflix.app.API.BEANLINKS;
import com.airaflix.app.Activities.Jdownload.DownloadManage;
import com.airaflix.app.Config.getComicsnextback;
import com.airaflix.app.DB.JseriesDB;
import com.airaflix.app.JPLAYER.JSPLAYER;
import com.airaflix.app.JPLAYER.WebPlayer;
import com.airaflix.app.Models.SeasonModel;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.Models.ServerModel;
import com.airaflix.app.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.inside4ndroid.jresolver.Jresolver;
import com.startapp.sdk.adsbase.StartAppAd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Episodes extends AppCompatActivity {
    Boolean isinepesode = false;
    public String typeofclicking = "watch";

    RecyclerView rectmovies;
    ProgressBar progreed;
    private SharedPreferences sp;
    StartAppAd startAppAd;
    List<ServerModel> epesodModels;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    Jresolver xGetter ;
    SerieModel tvmodel;
    SeasonModel seasonModel;
    ServerModel serverModel;
    ImageView backch;

    String postkeyEpe;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);



        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        rectmovies = findViewById(R.id.rectmovies);
        progreed = findViewById(R.id.progreed);
        backch = findViewById(R.id.backch);

        StartAppAd.init(this,"107796986","210473406");
        startAppAd = new StartAppAd(this);
        startAppAd.loadAd(StartAppAd.AdMode.VIDEO);
        InitiaStrings();

      /*  if (place.contains("movie")){
            GetEpesode();
        }else {
            GetSeasons();
        }*/
        GetEpesode();


    }

    private void GetSeasons() {





    }

    private void InitiaStrings() {
        Bundle extras = getIntent().getExtras();

        tvmodel = extras.getParcelable("tvmodel");
        seasonModel = extras.getParcelable("seasonmodel");
        serverModel = extras.getParcelable("episodesmodel");

        backch.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void GetEpesode() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Episodes.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rectmovies.setLayoutManager(linearLayoutManager);
        rectmovies.setNestedScrollingEnabled(false);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("User-Agent","Thunder Client (https://www.thunderclient.com)");



        AndroidNetworking.get(BEANLINKS.BASELINK+"movie/{id}")
                .addPathParameter("id", tvmodel.getId())
                .addHeaders(headers)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(SerieModel.class , new ParsedRequestListener<SerieModel>(){

                    @Override
                    public void onResponse(SerieModel movie) {



                        epesodModels = new ArrayList<>();
                        progreed.setVisibility(View.GONE);
                        for (ServerModel  epesode : movie.getVideos()) {
                            epesodModels.add(epesode);
                        }

                        getComicsnextback.Epesodes = epesodModels;
                        rectmovies.setAdapter(new epesoedAdapter(epesodModels,Episodes.this,tvmodel.getTitle()));

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });








    }

    public class epesoedAdapter extends RecyclerView.Adapter<epesoedAdapter.myviewholder> {

        List<ServerModel> chaptermodelList;
        Context context;
        String title;
        JseriesDB comicsdb;
        private SharedPreferences sp;

        public epesoedAdapter(List<ServerModel> chaptermodelList, Context context, String title) {
            this.chaptermodelList = chaptermodelList;
            this.context = context;
            this.title = title;
            comicsdb = new JseriesDB(context);
            sp = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        }

        @NonNull
        @Override
        public epesoedAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
            return new epesoedAdapter.myviewholder(view);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(@NonNull myviewholder holder, int position) {
            ServerModel c = chaptermodelList.get(position);


            int gg = position;
            String NameAndEpesoed = title+":"+c.getLebel();
            int currentPage = sp.getInt("currentPage"+NameAndEpesoed,1);
            int allduration = sp.getInt("allduration"+NameAndEpesoed,0);




            holder.episode.setText(c.getLebel());
            holder.titleof.setText(title);
            if (allduration < 6000){
                holder.showWhems.setMax(0);

            }else {
                holder.showWhems.setMax(allduration);
            }

            holder.showWhems.setProgress(currentPage);

            Glide.with(holder.itemView.getContext()).load(tvmodel.getPoster()).into(holder.thumbnail);


            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (c.getSource().equals("Mp4") || c.getSource().equals("M3u8")){


                        OpenDialogWatch(tvmodel,c,seasonModel,gg);


                    }else if(c.getSource().equals("Embed")  || c.getSource().equals("StreamSB")  || c.getSource().equals("StreamTape")  || c.getSource().equals("DoodStream")){

                        startActivity(new Intent(context, WebPlayer.class)
                                .putExtra("link",c.getUrl()));
                        CheckAds(Episodes.this);

                    } else  {


                        Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), EpesodTAFASIL.class);
                        sendDataToDetailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                        sendDataToDetailsActivity.putExtra("tvmodel",tvmodel);
                        sendDataToDetailsActivity.putExtra("seasonmodel",seasonModel);
                        sendDataToDetailsActivity.putExtra("episodesmodel", chaptermodelList.get(position));


                        holder.itemView.getContext().startActivity(sendDataToDetailsActivity);
                        CheckAds(Episodes.this);
                    }

                }
            });





            if(c.getSource().equals("Embed")  || c.getSource().equals("StreamSB")  || c.getSource().equals("StreamTape")  || c.getSource().equals("DoodStream")){
                holder.download_epe.setVisibility(View.GONE);
            }
            holder.download_epe.setOnClickListener(new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {

                    if (c.getSource().equals("Mp4") || c.getSource().equals("M3u8")){
                        CheckAds(Episodes.this);
                        DownloadManage.getInstance(context)
                                .addDownloadRequest(c.getUrl(),new File(""),title,c.getLebel());

                    }else if(c.getSource().equals("Embed")  || c.getSource().equals("StreamSB")  || c.getSource().equals("StreamTape")  || c.getSource().equals("DoodStream")){
                        startActivity(new Intent(context, WebPlayer.class)
                                .putExtra("link",c.getUrl()));
                    }else {
                        Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), EpesodTAFASIL.class);
                        sendDataToDetailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                        sendDataToDetailsActivity.putExtra("tvmodel",tvmodel);
                        sendDataToDetailsActivity.putExtra("seasonmodel",seasonModel);
                        sendDataToDetailsActivity.putExtra("episodesmodel", chaptermodelList.get(position));


                        holder.itemView.getContext().startActivity(sendDataToDetailsActivity);

                    }


                }
            });


        }





        @Override
        public int getItemCount() {
            return chaptermodelList.size();
        }

        public class myviewholder extends RecyclerView.ViewHolder {

            TextView episode,titleof;
            ImageView thumbnail,download_epe;
            LinearProgressIndicator showWhems;

            public myviewholder(@NonNull View itemView) {
                super(itemView);

                episode = itemView.findViewById(R.id.episode);
                titleof = itemView.findViewById(R.id.anime);
                thumbnail = itemView.findViewById(R.id.thumbnail);
                download_epe = itemView.findViewById(R.id.download_epe);
                showWhems = itemView.findViewById(R.id.showWhems);


            }


        }



    }


    private void OpenDialogWatch(SerieModel tvmodel, ServerModel c, SeasonModel seasonModel, int gg) {

        final Dialog dialog = new Dialog(Episodes.this, R.style.MyAlertDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sniffer);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;

        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.JSERIESPlayer).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                watchVideo(c.getUrl(),gg,tvmodel,seasonModel,c);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.vlc).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                if(tvmodel.getPlace().contains("tv")){
                    openWithVLC(c.getUrl(),tvmodel.getTitle() + " " + seasonModel.getName() + " " +c.getLebel(),tvmodel.getPoster());

                }else {
                    openWithVLC(c.getUrl(),tvmodel.getTitle() + " " + " " + " " +c.getLebel(),tvmodel.getPoster());

                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.mxPlayer).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                openWithMXPlayer(c.getUrl());
                dialog.dismiss();
            }
        });



        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void openWithVLC(String url, String s, String poster) {

        Intent shareVideo = new Intent(Intent.ACTION_VIEW);
        shareVideo.setDataAndType(Uri.parse(url), VIDEOTYPE);
        shareVideo.setPackage(VLC_PACKAGE_NAME);
        shareVideo.putExtra(TITLE, s);
        shareVideo.putExtra(POSTER, poster);
        Bundle headers = new Bundle();
        headers.putString(USER_AGENT, getResources().getString(R.string.app_name));
        shareVideo.putExtra(EXTRA_HEADERS, headers);
        shareVideo.putExtra(HEADERS, headers);
        shareVideo.putExtra(SECURE_URI, true);
        try {
            Episodes.this.startActivity(shareVideo);
        } catch (ActivityNotFoundException ex) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            String uriString = VLC_INTENT ;
            intent.setData(Uri.parse(uriString));
            Episodes.this.startActivity(intent);
        }

    }

    private void watchVideo(String c, int gg, SerieModel tvmodel, SeasonModel seasonModel, ServerModel serverModel) {
        Intent sendDataToDetailsActivity = new  Intent(this, JSPLAYER.class);
        sendDataToDetailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        sendDataToDetailsActivity.putExtra("tvmodel", this.tvmodel);
        sendDataToDetailsActivity.putExtra("seasonmodel",seasonModel);
        sendDataToDetailsActivity.putExtra("episodesmodel", serverModel);
        sendDataToDetailsActivity.putExtra("message","");
        sendDataToDetailsActivity.putExtra("position",String.valueOf(gg));

        startActivity(sendDataToDetailsActivity);
        CheckAds(Episodes.this);

    }

    public  void openWithMXPlayer(String url) {
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

    public boolean appInstalledOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}