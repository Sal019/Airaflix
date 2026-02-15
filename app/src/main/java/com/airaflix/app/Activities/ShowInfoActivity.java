package com.airaflix.app.Activities;

import static com.airaflix.app.API.BEANLINKS.APIKAY;
import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airaflix.app.API.BEANLINKS;
import com.airaflix.app.API.MovieApi;
import com.airaflix.app.API.Respons.MovieCastRespons;
import com.airaflix.app.API.Respons.MovieSearchRespons;
import com.airaflix.app.API.Respons.TMDB_API;
import com.airaflix.app.API.Servicy;
import com.airaflix.app.API.TMDBAPI;
import com.airaflix.app.Adapters.CastAdapter;
import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.Config.Function;
import com.airaflix.app.Config.getComicsnextback;
import com.airaflix.app.DB.JseriesDB;
import com.airaflix.app.Models.CastModel;
import com.airaflix.app.Models.SeasonModel;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.Models.ServerModel;
import com.airaflix.app.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowInfoActivity extends AppCompatActivity {



    String id,title,cover,poste,link,postkey,age,studio,date,desc
            ,cast,tasnif,mortabit,where,place,isfromeactibity,views,myAppItemId,youtubeid;
    Boolean ilikeit = false;


    ImageView gooback,likeicon,imgviews;
    TextView titleser,yeartxt,viewstxt,storytxt,genertxt,seasonscouct,typeofplacetxr;
    RelativeLayout watchepenoew;
    LinearLayout likeit;
    JseriesDB jseriesDB;
    MovieApi movieApi;
    TMDB_API tmdb_api;
    RecyclerView pin_rv,cast_rv;
    ProgressBar progpin,progcast;
    List<SerieModel> serieModelList;
    RelativeLayout pin_rlt,cast_rlt;

    static public StartAppAd startAppAd;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_show_info);
        movieApi = Servicy.getMovieApi();
        tmdb_api = TMDBAPI.getMovieApi();
        startAppAd = new StartAppAd(this);

        gooback = findViewById(R.id.gooback);
        pin_rlt = findViewById(R.id.pin_rlt);
        cast_rv = findViewById(R.id.cast_rv);
        cast_rlt = findViewById(R.id.cast_rlt);
        titleser = findViewById(R.id.titleser);
        yeartxt = findViewById(R.id.yeartxt);
        progcast = findViewById(R.id.progcast);
        pin_rv = findViewById(R.id.pin_rv);
        progpin = findViewById(R.id.progpin);
        viewstxt = findViewById(R.id.viewstxt);
        watchepenoew = findViewById(R.id.Gotoepusode);
        typeofplacetxr = findViewById(R.id.typeofplacetxr);
        storytxt = findViewById(R.id.storytxt);
        genertxt = findViewById(R.id.genertxt);
        seasonscouct = findViewById(R.id.seasonscouct);
        likeit = findViewById(R.id.likeit);
        likeicon = findViewById(R.id.likeicon);

        imgviews = findViewById(R.id.imgviews);
        jseriesDB = new JseriesDB(this);




        gooback.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        isfromeactibity = getIntent().getStringExtra("isintent");

        if (isfromeactibity != null){

            title = getIntent().getStringExtra("title");
            poste = getIntent().getStringExtra("poste");
            link = getIntent().getStringExtra("link");
            postkey = getIntent().getStringExtra("postkey");
            age = getIntent().getStringExtra("age");
            studio = getIntent().getStringExtra("studio");
            date = getIntent().getStringExtra("date");
            desc = getIntent().getStringExtra("desc");
            cast = getIntent().getStringExtra("cast");
            tasnif = getIntent().getStringExtra("tasnif");
            mortabit = getIntent().getStringExtra("mortabit");
            where = getIntent().getStringExtra("where");
            place = getIntent().getStringExtra("place");
            id = getIntent().getStringExtra("id");
            views = getIntent().getStringExtra("views");
            youtubeid = getIntent().getStringExtra("youtubeid");


            Glide.with(this).load(poste).into(imgviews);

            titleser.setText(title);
            yeartxt.setText(date);

            storytxt.setText(desc);
            genertxt.setText(tasnif);

            SerieModel serieModel = new SerieModel();
            serieModel.setTmdb_id(postkey);
            serieModel.setUpdated_at(youtubeid);
            serieModel.setViews(1);
            serieModel.setPoster(poste);
            serieModel.setYear(date);
            serieModel.setPlace(place);
            serieModel.setGener(tasnif);
            serieModel.setCountry(studio);
            serieModel.setAge(age);
            serieModel.setStory(desc);
            serieModel.setOther_season_id(mortabit);
            serieModel.setTitle(title);
            serieModel.setCast(cast);
            serieModel.setLink_id(link);
            CheckLikeStat(postkey);
            likeit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Likeit(serieModel);
                }
            });




           GetFierstEpisode(id);
           GetMoreLikeThis(postkey,tasnif);
           GetCasts(postkey,place);

           


        }else {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(ShowInfoActivity.this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink;
                            deepLink = pendingDynamicLinkData.getLink();
                            myAppItemId = deepLink.getQueryParameter("id");

                            DatabaseReference db = allseriesandmovies.child(myAppItemId);
                            db.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snap) {



                                    title = snap.child("title").getValue(String.class);
                                    id = snap.child("id").getValue(String.class);
                                    desc = snap.child("story").getValue(String.class);
                                    poste = snap.child("poster").getValue(String.class);
                                    link = snap.child("link_id").getValue(String.class);
                                    date = snap.child("year").getValue(String.class);
                                    mortabit = snap.child("other_season_id").getValue(String.class);
                                    studio = snap.child("country").getValue(String.class);
                                    age = snap.child("age").getValue(String.class);
                                    place = snap.child("place").getValue(String.class);
                                    tasnif = snap.child("gener").getValue(String.class);
                                    postkey = snap.child("serie_id").getValue(String.class);
                                    cast = snap.child("cast").getValue(String.class);

                                    Glide.with(ShowInfoActivity.this).load(poste).into(imgviews);

                                    titleser.setText(title);
                                    yeartxt.setText(date);

                                    storytxt.setText(desc);
                                    genertxt.setText(tasnif);

                                    SerieModel serieModel = new SerieModel();
                                    serieModel.setTmdb_id(myAppItemId);
                                    serieModel.setUpdated_at(youtubeid);
                                    serieModel.setViews(1);
                                    serieModel.setPoster(poste);
                                    serieModel.setYear(date);
                                    serieModel.setPlace(place);
                                    serieModel.setGener(tasnif);
                                    serieModel.setCountry(studio);
                                    serieModel.setAge(age);
                                    serieModel.setStory(desc);
                                    serieModel.setOther_season_id(mortabit);
                                    serieModel.setTitle(title);
                                    serieModel.setCast(cast);
                                    serieModel.setLink_id(link);

                                    likeit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Likeit(serieModel);
                                        }
                                    });


                                    CheckLikeStat(myAppItemId);

                                   // GetFierstEpisode(link);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    private void GetCasts(String id, String place) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        cast_rv.setLayoutManager(linearLayoutManager);




        if (place.contains("tv")){
            Call<MovieCastRespons> responsCall = tmdb_api
                    .getTVCast(
                            Function.ToInterger(id),
                            APIKAY
                    );

            responsCall.enqueue(new Callback<MovieCastRespons>() {
                @Override
                public void onResponse(Call<MovieCastRespons> call, Response<MovieCastRespons> response) {
                    if (response.code()== 200){
                        Log.v("ra","rrr  " + response.body().toString());
                        progcast.setVisibility(View.GONE);
                        List<CastModel> casts = new ArrayList<>(response.body().getCasts());
                        if (casts.size() > 0){
                            cast_rlt.setVisibility(View.VISIBLE);
                        }else {
                            cast_rlt.setVisibility(View.GONE);

                        }
                        cast_rv.setAdapter(new CastAdapter(ShowInfoActivity.this,casts));


                    }
                }

                @Override
                public void onFailure(Call<MovieCastRespons> call, Throwable t) {

                }
            });
        }else {
            Call<MovieCastRespons> responsCall = tmdb_api
                    .getMovieCast(
                            Function.ToInterger(id),
                            APIKAY
                    );

            responsCall.enqueue(new Callback<MovieCastRespons>() {
                @Override
                public void onResponse(Call<MovieCastRespons> call, Response<MovieCastRespons> response) {
                    if (response.code()== 200){
                        Log.v("ra","rrr  " + response.body().toString());
                        progcast.setVisibility(View.GONE);
                        List<CastModel> casts = new ArrayList<>(response.body().getCasts());
                        if (casts.size() > 0){
                            cast_rlt.setVisibility(View.VISIBLE);
                        }else {
                            cast_rlt.setVisibility(View.GONE);

                        }
                        cast_rv.setAdapter(new CastAdapter(ShowInfoActivity.this,casts));


                    }
                }

                @Override
                public void onFailure(Call<MovieCastRespons> call, Throwable t) {

                }
            });

        }





    }

    private void GetMoreLikeThis(String postkey, String tasnif) {

        LinearLayoutManager ll = new LinearLayoutManager(ShowInfoActivity.this);
        ll.setOrientation(RecyclerView.HORIZONTAL);
        pin_rv.setLayoutManager(ll);


            Call<MovieSearchRespons> responsCall = movieApi
                    .getAllMoviesAndTVs();


            responsCall.enqueue(new Callback<MovieSearchRespons>( ) {
                @Override
                public void onResponse(Call<MovieSearchRespons> call, Response<MovieSearchRespons> response) {

                    List<SerieModel> Series = new ArrayList<>(response.body().getMovies());
                    serieModelList = new ArrayList<>(  );


                    for (SerieModel serieModel: Series) {
                        if ( !serieModel.getTmdb_id().equals(postkey) && serieModel.getPlace().contains("Published") ){
                            serieModelList.add(serieModel);
                        }
                    }


                    if (serieModelList.size() > 0){
                        pin_rlt.setVisibility(View.VISIBLE);
                        progpin.setVisibility(View.GONE);
                        Collections.shuffle(serieModelList);
                        pin_rv.setAdapter(new SeriesAdapter(ShowInfoActivity.this,serieModelList,0));


                    }else {
                        pin_rlt.setVisibility(View.GONE);

                    }



                }

                @Override
                public void onFailure(Call<MovieSearchRespons> call, Throwable t) {

                }
            });







    }


    private void GetFierstEpisode(String link) {
        List<ServerModel> firestEpe = new ArrayList<>();
        List<SeasonModel> Seasons = new ArrayList<>();


   /*     Call<SerieModel> responsCall = movieApi
                .getMovieDetiels(
                        Function.ToInterger(link)
                );

        responsCall.enqueue(new Callback<SerieModel>( ) {
            @Override
            public void onResponse(Call<SerieModel> call, Response<SerieModel> response) {
               
                if (response.code() == 200){
                    SerieModel movie = response.body();

                    String formatted = format(movie.getViews());
                    viewstxt.setText(formatted);


                    seasonscouct.setText("MOVIE");

                    for (ServerModel episode: movie.getVideos()) {
                        Log.d("TAG", "onResponse: "+episode);
                        firestEpe.add(episode);
                        getComicsnextback.Epesodes.add(episode);
                    }


                }
            }

            @Override
            public void onFailure(Call<SerieModel> call, Throwable t) {

            }
        });
*/


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("User-Agent","Thunder Client (https://www.thunderclient.com)");


        if (!place.contains("tv")){
            watchepenoew.setEnabled(false);

            AndroidNetworking.get(BEANLINKS.BASELINK+"movie/{id}")
                    .addPathParameter("id", link)
                    .addHeaders(headers)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsObject(SerieModel.class , new ParsedRequestListener<SerieModel>(){

                        @Override
                        public void onResponse(SerieModel movie) {

                            String formatted = format(movie.getViews());
                            viewstxt.setText(formatted);




                            seasonscouct.setText("MOVIE");

                            for (ServerModel episode: movie.getVideos()) {
                                firestEpe.add(episode);
                                getComicsnextback.Epesodes.add(episode);
                            }

                            watchepenoew.setEnabled(true);

                            watchepenoew.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Intent sendDataToDetailsActivity = new Intent(ShowInfoActivity.this, Episodes.class);
                                    sendDataToDetailsActivity.putExtra("tvmodel",movie);

                                    startActivity(sendDataToDetailsActivity);
                                    CheckAds(ShowInfoActivity.this);
                                }
                            });
                        }


                        @Override
                        public void onError(ANError anError) {

                        }
                    });


        }else {
            watchepenoew.setEnabled(false);
            typeofplacetxr.setText("Seasons");
            AndroidNetworking.get(BEANLINKS.BASELINK+"tv/{id}")
                    .addPathParameter("id", link)
                    .addHeaders(headers)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsObject(SerieModel.class , new ParsedRequestListener<SerieModel>(){

                        @Override
                        public void onResponse(SerieModel movie) {

                            String formatted = format(movie.getViews());
                            viewstxt.setText(formatted);




                            seasonscouct.setText( movie.getSeasons().size()+" Seasons");

                            for (SeasonModel episode: movie.getSeasons()) {
                                Seasons.add(episode);
                                getComicsnextback.SeasonsOut.add(episode);
                            }
                            watchepenoew.setEnabled(true);

                            watchepenoew.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {



                                    Intent sendDataToDetailsActivity = new Intent(ShowInfoActivity.this, MySeasons.class);

                                    sendDataToDetailsActivity.putExtra("tvmodel",movie);

                                    startActivity(sendDataToDetailsActivity);
                                    CheckAds(ShowInfoActivity.this);

                                }
                            });
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


        }









    }

    static public void CheckAds(Activity context) {
        MaxRewardedAd videoAd = MaxRewardedAd.getInstance( context.getResources().getString(R.string.adUnitId_Applovin), context );

        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC, new AdEventListener( ) {
            @Override
            public void onReceiveAd(@NonNull Ad ad) {
                startAppAd.showAd();
            }

            @Override
            public void onFailedToReceiveAd(@Nullable Ad ad) {
                videoAd.setListener(new MaxRewardedAdListener() {
                    @Override
                    public void onRewardedVideoStarted(MaxAd ad) {
                    }

                    @Override
                    public void onRewardedVideoCompleted(MaxAd ad) {

                    }

                    @Override
                    public void onUserRewarded(MaxAd ad, MaxReward reward) {

                    }

                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        if(videoAd.isReady()){
                            videoAd.showAd();

                        }
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {

                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                    }
                });

                videoAd.loadAd();
            }
        });


    }

    private void ShowADmob() {



    }

    @Override
    protected void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startAppAd.onPause();
    }



    private void Likeit(SerieModel serieModel) {



        if (jseriesDB.checkIfMyListExists(postkey)){

            boolean added = jseriesDB.DeletMyListDB(postkey);
            if (added){
                Toast.makeText(ShowInfoActivity.this, "Removed from My List", Toast.LENGTH_SHORT).show();
                likeicon.setImageDrawable(getResources().getDrawable(R.drawable.pluss));
                CheckLikeStat(serieModel.getTmdb_id());
            }
        }else {

            boolean added = jseriesDB.AddtoMyListdDB(serieModel);
            if (added){
                Toast.makeText(ShowInfoActivity.this, "Added To My List", Toast.LENGTH_SHORT).show();
                likeicon.setImageDrawable(getResources().getDrawable(R.drawable.check));
                CheckLikeStat(serieModel.getTmdb_id());
            }

        }
    }

    private void CheckLikeStat(String postkey) {

        if (jseriesDB.checkIfMyListExists(postkey)){

            likeicon.setImageDrawable(getResources().getDrawable(R.drawable.check));

        }else {

            likeicon.setImageDrawable(getResources().getDrawable(R.drawable.pluss));

        }


    }


/*
    private void initiViewPager() {

        gooback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        myViewPagerAdapter = new MyViewPagerAdapterInfo(ShowInfoActivity.this);
        viewPager2.setAdapter(myViewPagerAdapter);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    public class MyViewPagerAdapterInfo extends FragmentStateAdapter {
        public MyViewPagerAdapterInfo(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){


                case 1:
                    return new morelikeit().newInstance(title,cover,poste,postkey,age,studio,date
                            ,desc,cast,tasnif,where,mortabit,link,place,views);

                default:
                    return new castinfo().newInstance(title,cover,poste,postkey,age,studio,date
                        ,desc,cast,tasnif,where,mortabit,link,place,views);


            }

        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
*/

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

}