package com.airaflix.app.Activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.airaflix.app.Activities.EpesodTAFASIL.EXTRA_HEADERS;
import static com.airaflix.app.Activities.EpesodTAFASIL.HEADERS;
import static com.airaflix.app.Activities.EpesodTAFASIL.POSTER;
import static com.airaflix.app.Activities.EpesodTAFASIL.SECURE_URI;
import static com.airaflix.app.Activities.EpesodTAFASIL.TITLE;
import static com.airaflix.app.Activities.EpesodTAFASIL.USER_AGENT;
import static com.airaflix.app.Activities.EpesodTAFASIL.VIDEOTYPE;
import static com.airaflix.app.Activities.MyList.Mylist.changestatucolor;
import static com.airaflix.app.Activities.ShowInfoActivity.CheckAds;
import static com.airaflix.app.Config.config.SETTINGS_NAME;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airaflix.app.API.BEANLINKS;
import com.airaflix.app.Activities.Jdownload.DownloadManage;
import com.airaflix.app.Config.ServerController;
import com.airaflix.app.Config.getComicsnextback;
import com.airaflix.app.DB.JseriesDB;
import com.airaflix.app.Extracters.jresolver.Jresolver;
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
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.startapp.sdk.adsbase.StartAppAd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySeasons extends AppCompatActivity {

    RecyclerView rectmovies;
    ProgressBar progreed;
    private SharedPreferences sp;
    StartAppAd startAppAd;
    List<SeasonModel> epesodModels;
    List<ServerModel> serverModels;

    //Spinner choseplacesave;
    AutoCompleteTextView auto_complet;
    TextInputLayout nbreplace;
    ImageView backch;

    public static final String VLC_INTENT = "market://details?id=org.videolan.vlc";
    public static final String VLC_PACKAGE_NAME = "org.videolan.vlc";

    String epetitle;
    String postkeyEpe;
    TextView namechrabar;
    Jresolver jresolver;
    ServerController serverController;
    SerieModel tvmodel;

    @SuppressLint("MissingInflatedId")
    HashMap<String, String> headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_seasons);
        changestatucolor(this);


        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        serverController = new ServerController(MySeasons.this);

        rectmovies = findViewById(R.id.rectmovies);
        auto_complet = findViewById(R.id.auto_complet);
        progreed = findViewById(R.id.progreed);
        nbreplace = findViewById(R.id.nbreplace);
        backch = findViewById(R.id.backch);
        namechrabar = findViewById(R.id.namechrabar);
        //choseplacesave = findViewById(R.id.choseplacesave);

        StartAppAd.init(this,"107796986","210473406");
        startAppAd = new StartAppAd(this);
        startAppAd.loadAd(StartAppAd.AdMode.VIDEO);
        InitiaStrings();
        headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("User-Agent","Thunder Client (https://www.thunderclient.com)");
        namechrabar.setText("Episodes ("+tvmodel.getTitle()+" )");
        backch.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetSeasons();

        GetLinks();
    }

    private void GetSeasons() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MySeasons.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rectmovies.setLayoutManager(linearLayoutManager);
        rectmovies.setNestedScrollingEnabled(false);


        nbreplace.setVisibility(View.GONE);


        AndroidNetworking.get(BEANLINKS.BASELINK+"tv/{id}")
                .addPathParameter("id", tvmodel.getId( ))
                .addHeaders(headers)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(SerieModel.class , new ParsedRequestListener<SerieModel>(){

                    @Override
                    public void onResponse(SerieModel movie) {


                        epesodModels = new ArrayList<>();
                        ArrayList<String> seasonstilte = new ArrayList<>(  );
                        progreed.setVisibility(View.GONE);
                        for (SeasonModel  epesode : movie.getSeasons()) {
                            epesodModels.add(epesode);
                            seasonstilte.add(epesode.getName());
                        }


                        getComicsnextback.SeasonsOut.clear();
                        getComicsnextback.SeasonsOut = epesodModels;

                        nbreplace.setVisibility(View.VISIBLE);



                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MySeasons.this,
                                R.layout.custom_spinner,seasonstilte);

                        if (epesodModels.size() > 0){
                            auto_complet.setAdapter(adapter);

                            auto_complet.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    GetEpisdoeFromSeason(epesodModels.get(position),seasonstilte.get(position));
                                }
                            });
                        }



                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private void GetEpisdoeFromSeason(SeasonModel seasonModel, String seasonname) {


        progreed.setVisibility(View.VISIBLE);
        serverModels = new ArrayList<>();
        AndroidNetworking.get(BEANLINKS.BASELINK+"episdoesofseason/{id}")
                .addPathParameter("id", String.valueOf(seasonModel.getId()))
                .addHeaders(headers)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(ServerModel.class , new ParsedRequestListener<List<ServerModel>>(){

                    @Override
                    public void onResponse(List<ServerModel> movie) {

                        progreed.setVisibility(View.GONE);
                        for (ServerModel  epesode : movie) {
                            serverModels.add(epesode);
                        }

                        getComicsnextback.Epesodestv.clear();
                        getComicsnextback.Epesodes.clear();
                        getComicsnextback.Epesodestv = serverModels;
                        getComicsnextback.Epesodes = serverModels;
                        rectmovies.setAdapter(new epesoedSeasonsAdapter(
                                serverModels,
                                MySeasons.this,
                                tvmodel.getTitle(),
                                seasonname,
                                seasonModel));


                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private void GetLinks() {




    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    //perform
                } else {
                    //request
                }
            });
    private void InitiaStrings() {

        Bundle extras = getIntent().getExtras();

         tvmodel = extras.getParcelable("tvmodel");


    }


    public class epesoedSeasonsAdapter extends RecyclerView.Adapter<epesoedSeasonsAdapter.myviewholder> {

        List<ServerModel> chaptermodelList;
        SeasonModel seasonModel;
        Context context;
        String title,seasonname;
        JseriesDB comicsdb;
        private SharedPreferences sp;
        public epesoedSeasonsAdapter(List<ServerModel> chaptermodelList, Context context, String title, String seasonname,SeasonModel seasonModel) {
            this.chaptermodelList = chaptermodelList;
            this.context = context;
            this.title = title;
            this.seasonname = seasonname;
            this.seasonModel = seasonModel;
            comicsdb = new JseriesDB(context);
            sp = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        }

        @NonNull
        @Override
        public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
            return new myviewholder(view);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(@NonNull myviewholder holder, int position) {
            ServerModel c = chaptermodelList.get(position);


            int gg = position;
            String NameAndEpesoed = title+":"+c.getLebel();
            int currentPage = sp.getInt("currentPage"+NameAndEpesoed,1);
            int allduration = sp.getInt("allduration"+NameAndEpesoed,0);
            epetitle = c.getLebel();





            holder.episode.setText(epetitle);
            holder.titleof.setText(title + " " + seasonname);
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

                        /*EasyPlexSupportedHosts easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);
                        easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted( ) {
                            @Override
                            public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> arrayList, boolean b) {
                                Log.d("TAG", "onTaskCompleted: "+arrayList);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                        easyPlexSupportedHosts.find(c.getUrl());*/

                        startActivity(new Intent(context, WebPlayer.class)
                                .putExtra("link",c.getUrl()));
                        CheckAds(MySeasons.this);

                    } else  {


                        Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), EpesodTAFASIL.class);
                        sendDataToDetailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        sendDataToDetailsActivity.putExtra("position",String.valueOf(gg));

                        sendDataToDetailsActivity.putExtra("tvmodel",tvmodel);
                        sendDataToDetailsActivity.putExtra("seasonmodel",seasonModel);
                        sendDataToDetailsActivity.putExtra("episodesmodel", chaptermodelList.get(position));


                        holder.itemView.getContext().startActivity(sendDataToDetailsActivity);
                        CheckAds(MySeasons.this);
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


                        DownloadManage.getInstance(context)
                                .addDownloadRequest(c.getUrl(),new File(""),title + " ( "+seasonname+" ) ",c.getLebel());



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

        final Dialog dialog = new Dialog(MySeasons.this, R.style.MyAlertDialogTheme);
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
                CheckAds(MySeasons.this);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.vlc).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                openWithVLC(c.getUrl(),tvmodel.getTitle() + " " + seasonModel.getName() + " " +c.getLebel(),tvmodel.getPoster());
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
            MySeasons.this.startActivity(shareVideo);
        } catch (ActivityNotFoundException ex) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            String uriString = VLC_INTENT ;
            intent.setData(Uri.parse(uriString));
            MySeasons.this.startActivity(intent);
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