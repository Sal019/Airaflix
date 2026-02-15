package com.airaflix.app.JPLAYER;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

import static com.airaflix.app.Activities.EpesodTAFASIL.EXTRA_HEADERS;
import static com.airaflix.app.Activities.EpesodTAFASIL.HEADERS;
import static com.airaflix.app.Activities.EpesodTAFASIL.POSTER;
import static com.airaflix.app.Activities.EpesodTAFASIL.SECURE_URI;
import static com.airaflix.app.Activities.EpesodTAFASIL.TITLE;
import static com.airaflix.app.Activities.EpesodTAFASIL.USER_AGENT;
import static com.airaflix.app.Activities.EpesodTAFASIL.VIDEOTYPE;
import static com.airaflix.app.Activities.MySeasons.VLC_INTENT;
import static com.airaflix.app.Activities.MySeasons.VLC_PACKAGE_NAME;
import static com.airaflix.app.Activities.Settings.SettingsJcartoon.SETTINGS_NAME;
import static com.airaflix.app.Config.SwipyRefreshLayout.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airaflix.app.Activities.EpesodTAFASIL;
import com.airaflix.app.Activities.Jdownload.DownloadManage;
import com.airaflix.app.Activities.MySeasons;
import com.airaflix.app.Config.Function;
import com.airaflix.app.Config.Utils;
import com.airaflix.app.Config.config;
import com.airaflix.app.Config.getComicsnextback;
import com.airaflix.app.DB.JseriesDB;
import com.airaflix.app.Models.CountinuModel;
import com.airaflix.app.Models.SeasonModel;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.Models.ServerModel;
import com.airaflix.app.R;
import com.bumptech.glide.Glide;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.TracksInfo;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoSize;
import com.google.android.material.progressindicator.LinearProgressIndicator;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class JSPLAYER extends AppCompatActivity {

    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector trackSelector;
    String[] speed = {"0.25x","0.5x","Normal","1.5x","2x"};
    String NameAndEpesoed;

    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView cartoontitle,cartoonepesod,et_url;
    ImageView videoView_go_back;
    ProgressBar prgorssd;
    LinearLayout ellepisodes,videoView_one_layout,videoView_three_layout
            ,videoView_four_layout,resizethevid;
    ImageView imageView5,imageView8;
    Long mLastPosition;
    String position,message;
    String eName;
    int epePOSIT;


    private SharedPreferences sp;
    private int currentPage;
    private int allduration;


    boolean flag = false;

    Handler controllerHandler;

    AudioManager audioManager;

    View volumeMenu;

    View forward;
    View backward;

    float y;
    float dy;

    int maxVolume = 0;
    int currentVolume = 0;



    ProgressDialog progressDialog;
    JseriesDB comicsdb;

    RecyclerView rectmovies;
    ImageView go_close;
    RelativeLayout allepisodeslayout;

    SerieModel tvmodel;
    SeasonModel seasonModel;
    ServerModel serverModel;
    List<ServerModel> myserver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_jplayer);
        sp = getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
        comicsdb = new JseriesDB(JSPLAYER.this);

        setSystemBarTransparent(this);
        hideSystemPlayerUi(this,true,0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Inisiaktiintent();

        if (message.equals("local")){

            eName = getIntent().getStringExtra("eName");

        }else {
            eName = serverModel.getLebel();

        }

        if (seasonModel == null){
            if (message.equals("local")){

                NameAndEpesoed = getIntent().getStringExtra("name")+":"+getIntent().getStringExtra("eName");

            }else {
                NameAndEpesoed = tvmodel.getTitle()+":"+serverModel.getLebel();

            }

        }else {
            NameAndEpesoed = tvmodel.getTitle()+" ( "+seasonModel.getName()+" ) "+":"+serverModel.getLebel();

        }
        currentPage = sp.getInt("currentPage"+NameAndEpesoed,1);
        allduration = sp.getInt("allduration"+NameAndEpesoed,1);
        epePOSIT = Function.ToInterger(position);

        playerView = findViewById(R.id.jplayer);
        allepisodeslayout = findViewById(R.id.allepisodeslayout);
        go_close = findViewById(R.id.go_close);
        rectmovies = findViewById(R.id.rectmovies);
        cartoontitle = findViewById(R.id.cartoontitle);
        cartoonepesod = findViewById(R.id.cartoonepesod);
        videoView_go_back = findViewById(R.id.videoView_go_back);
        prgorssd = findViewById(R.id.prgorssd);
        imageView5 = findViewById(R.id.imageView5);
        imageView8 = findViewById(R.id.imageView8);
        ellepisodes = findViewById(R.id.ellepisodes);
        videoView_three_layout = findViewById(R.id.videoView_three_layout);
        videoView_four_layout = findViewById(R.id.videoView_four_layout);
        videoView_one_layout = findViewById(R.id.videoView_one_layout);
        ImageButton farwordBtn = playerView.findViewById(R.id.fwd);
        ImageButton rewBtn = playerView.findViewById(R.id.rew);
        LinearLayout NextEpebtn = playerView.findViewById(R.id.nextepisodes);
        TextView speedTxt = playerView.findViewById(R.id.speed);
        volumeMenu = findViewById(R.id.volume_menu);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        controllerHandler = new Handler();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rectmovies.setLayoutManager(linearLayoutManager);
        rectmovies.setNestedScrollingEnabled(false);

        if (eName == null){
            cartoonepesod.setVisibility(View.GONE);
        }

        videoView_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory());
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        playerView.setPlayer(simpleExoPlayer);





        if(message.equals("local")){
            SetEpeLinks(getIntent().getStringExtra("url"),getIntent().getStringExtra("name"),eName,message);


        }else {
            SetEpeLinks(serverModel.getUrl(),tvmodel.getTitle(),eName,message);



        }


        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {

            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {

            }

            @Override
            public void onTracksInfoChanged(TracksInfo tracksInfo) {

            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onAvailableCommandsChanged(Player.Commands availableCommands) {

            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {

                if (playbackState == Player.STATE_BUFFERING){
                    prgorssd.setVisibility(View.VISIBLE);

                }else if (playbackState == Player.STATE_READY){
                    prgorssd.setVisibility(View.GONE);

                }

            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {

            }

            @Override
            public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(PlaybackException error) {

            }

            @Override
            public void onPlayerErrorChanged(@Nullable PlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekForwardIncrementChanged(long seekForwardIncrementMs) {

            }

            @Override
            public void onSeekBackIncrementChanged(long seekBackIncrementMs) {

            }

            @Override
            public void onAudioSessionIdChanged(int audioSessionId) {

            }

            @Override
            public void onAudioAttributesChanged(AudioAttributes audioAttributes) {

            }

            @Override
            public void onVolumeChanged(float volume) {

            }

            @Override
            public void onSkipSilenceEnabledChanged(boolean skipSilenceEnabled) {

            }

            @Override
            public void onDeviceInfoChanged(DeviceInfo deviceInfo) {

            }

            @Override
            public void onDeviceVolumeChanged(int volume, boolean muted) {

            }

            @Override
            public void onEvents(Player player, Player.Events events) {

            }

            @Override
            public void onVideoSizeChanged(VideoSize videoSize) {

            }

            @Override
            public void onSurfaceSizeChanged(int width, int height) {

            }

            @Override
            public void onRenderedFirstFrame() {

            }

            @Override
            public void onCues(List<Cue> cues) {

            }

            @Override
            public void onMetadata(Metadata metadata) {

            }

            @Override
            public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {

            }

            @Override
            public void onPlaylistMetadataChanged(MediaMetadata mediaMetadata) {

            }
        });





        if ( getComicsnextback.Epesodestv.isEmpty()){
            NextEpebtn.setVisibility(View.GONE);
            ellepisodes.setVisibility(View.GONE);
        }else {
            myserver = new ArrayList<>(  );
            NextEpebtn.setVisibility(View.VISIBLE);
            ellepisodes.setVisibility(View.VISIBLE);

            NextEpebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (epePOSIT == getComicsnextback.Epesodestv.size()-1 ){
                        Toast.makeText(JSPLAYER.this, "This is the last episode on list", Toast.LENGTH_SHORT).show();
                    }else {
                        epePOSIT++;
                        NameAndEpesoed = getIntent().getStringExtra("name")+":"+getComicsnextback.Epesodestv.get(epePOSIT).getLebel();

                        SetEpeLinks(
                                getComicsnextback.Epesodestv.get(epePOSIT).getUrl(),
                                tvmodel.getTitle(),
                                getComicsnextback.Epesodestv.get(epePOSIT).getLebel(),
                                message);
                    }


                }
            });
            go_close.setOnClickListener(new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    allepisodeslayout.setVisibility(View.GONE);
                }
            });

            ellepisodes.setOnClickListener(new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    allepisodeslayout.setVisibility(View.VISIBLE);
                    playerView.hideController();
                    myserver.clear();
                    for (ServerModel server : getComicsnextback.Epesodestv){
                        if (!server.getId().equals(serverModel.getId()))
                            myserver.add(server);
                    }


                    rectmovies.setAdapter(new myepesoedSeasonsAdapter(
                            myserver,
                            JSPLAYER.this,
                            tvmodel.getTitle(),
                            seasonModel.getName(),
                            seasonModel));



                }
            });

        }



        farwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 10000);

            }
        });

        rewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long num = simpleExoPlayer.getCurrentPosition() - 10000;
                if (num < 0) {

                    simpleExoPlayer.seekTo(0);


                } else {

                    simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - 10000);

                }


            }
        });

    }

    private void Inisiaktiintent() {


        Intent i = getIntent();

        Bundle extras = getIntent().getExtras();

        tvmodel = extras.getParcelable("tvmodel");
        seasonModel = extras.getParcelable("seasonmodel");
        serverModel = extras.getParcelable("episodesmodel");
        message = i.getStringExtra("message");
        position = getIntent().getStringExtra("position");


    }

    private void SaveinDB() {

        if (tvmodel.getPlace().contains("tv")){
            CountinuModel comicsModel = new CountinuModel();
            comicsModel.setId(tvmodel.getId());
            comicsModel.setTitle(tvmodel.getTitle());
            comicsModel.setPoster(tvmodel.getPoster());
            comicsModel.setCover(tvmodel.getCover());
            comicsModel.setYear(tvmodel.getYear());
            comicsModel.setPlace(tvmodel.getPlace());
            comicsModel.setGener(tvmodel.getGener());
            comicsModel.setTmdb_id(tvmodel.getTmdb_id());
            comicsModel.setCountry(tvmodel.getCountry( ));
            comicsModel.setAge(tvmodel.getAge());
            comicsModel.setStory(tvmodel.getStory());


            comicsModel.setSeason_id(seasonModel.getId());
            comicsModel.setSeason_name(seasonModel.getName());
            comicsModel.setSeason_episodes(seasonModel.getEpisodes());
            comicsModel.setSeason_status(seasonModel.getStatus());



            comicsModel.setServer_id(serverModel.getId());
            comicsModel.setServer_lebel(serverModel.getLebel());
            comicsModel.setServer_source(serverModel.getSource());
            comicsModel.setServer_url(serverModel.getUrl());

            comicsModel.setPosition(position);
            comicsModel.setCurrent_position(String.valueOf(simpleExoPlayer.getCurrentPosition()));
            comicsModel.setFull_duration(String.valueOf(simpleExoPlayer.getDuration()));


            if (!comicsdb.checkIfCountineExists(comicsModel.getId())){
                comicsdb.AddCountineWatchingDB(comicsModel);

            }else {
                comicsdb.UpdateCountinuDB(comicsModel);

            }
        }else {
            CountinuModel comicsModel = new CountinuModel();
            comicsModel.setId(tvmodel.getId());
            comicsModel.setTitle(tvmodel.getTitle());
            comicsModel.setPoster(tvmodel.getPoster());
            comicsModel.setCover(tvmodel.getCover());
            comicsModel.setYear(tvmodel.getYear());
            comicsModel.setPlace(tvmodel.getPlace());
            comicsModel.setGener(tvmodel.getGener());
            comicsModel.setTmdb_id(tvmodel.getTmdb_id());
            comicsModel.setCountry(tvmodel.getCountry( ));
            comicsModel.setAge(tvmodel.getAge());
            comicsModel.setStory(tvmodel.getStory());


            comicsModel.setServer_id(serverModel.getId());
            comicsModel.setServer_lebel(serverModel.getLebel());
            comicsModel.setServer_source(serverModel.getSource());
            comicsModel.setServer_url(serverModel.getUrl());

            comicsModel.setPosition(position);
            comicsModel.setCurrent_position(String.valueOf(simpleExoPlayer.getCurrentPosition()));
            comicsModel.setFull_duration(String.valueOf(simpleExoPlayer.getDuration()));

            Log.d(TAG, "SaveinDB: " + tvmodel.getTmdb_id());

            if (!comicsdb.checkIfCountineExists(comicsModel.getId())){
                comicsdb.AddCountineWatchingDB(comicsModel);

            }else {
                comicsdb.UpdateCountinuDB(comicsModel);

            }
        }


    }


    public void showVolumeMenu(){
        View volumeBar = findViewById(R.id.volume_bar);
        TextView volumeText = findViewById(R.id.volume_text);

        int mV = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int cV = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int barH = (Utils.DpToPx(100, this) / mV) * cV;

        volumeText.setText("" + cV);
        volumeBar.getLayoutParams().height = barH;
        volumeBar.requestLayout();

        volumeMenu.setVisibility(View.VISIBLE);
    }

    public void hideVolumeMenu(){
        volumeMenu.setVisibility(View.INVISIBLE);
    }

    boolean doubleClick = false;


    private void SetEpeLinks(String url,String name,String epeNumber,String message) {



        if (seasonModel == null){
            cartoontitle.setText(name);

        }else {
            cartoontitle.setText(name +" ( "+seasonModel.getName()+" )");

        }

        cartoonepesod.setText(epeNumber);

        MediaItem videoSource = null;
        MediaSource mediaSource = null;


        if(message.equals("")) {
            Uri video1 = Uri.parse(url);
            videoSource = MediaItem.fromUri(video1);
            mediaSource = buildMediaSource(video1);
        }else if(message.equals("local")){
            Uri video1 = Uri.fromFile(new File(url));
            videoSource = MediaItem.fromUri(video1);
            mediaSource = buildMediaSource(video1);
        }



        //simpleExoPlayer.addMediaItem(videoSource);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        if (currentPage == 0){
            simpleExoPlayer.play();

        }else {
            simpleExoPlayer.seekTo(currentPage);
            simpleExoPlayer.play();

        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    y = event.getY();

                    maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    dy = event.getY() - y;

                    if(Math.abs(dy) > 10) {
                        float delta = dy / (Math.abs(width - height) / 2);
                        int volume = (int) (delta * maxVolume * -1);

                        if (volume > maxVolume - currentVolume) {
                            volume = maxVolume - currentVolume;
                        }
                        if (volume < -currentVolume) {
                            volume = -currentVolume;
                        }

                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + volume, 0);

                        showVolumeMenu();
                    }
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    hideVolumeMenu();
                }
                return false;
            }
        });

    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                JSPLAYER.this, Util.getUserAgent(JSPLAYER.this, "jseries"));
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
    }

    public static void setSystemBarTransparent(Activity act) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }


    // the system bars on Player
    public static void hideSystemPlayerUi(@NonNull final Activity activity, final boolean immediate) {
        hideSystemPlayerUi(activity, immediate, 5000);
    }

    // This snippet hides the system bars for api 30 or less
    @SuppressLint("ObsoleteSdkInt")
    public static void hideSystemPlayerUi(@NonNull final Activity activity, final boolean immediate, final int delayMs) {


        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        View decorView = activity.getWindow().getDecorView();
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        int uiState = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
        if (Util.SDK_INT > 18) {
            uiState |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        } else {
            final Handler handler = new Handler(Looper.getMainLooper());
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if (visibility == View.VISIBLE) {
                    Runnable runnable = () -> hideSystemPlayerUi(activity, false);
                    if (immediate) {
                        handler.post(runnable);
                    } else {
                        handler.postDelayed(runnable, delayMs);
                    }
                }
            });
        }
        decorView.setSystemUiVisibility(uiState);

    }

    private void showDefaultControls() {

        videoView_go_back.setVisibility(View.VISIBLE);
        videoView_one_layout.setVisibility(View.VISIBLE);
        imageView5.setVisibility(View.VISIBLE);
        imageView8.setVisibility(View.VISIBLE);
        videoView_three_layout.setVisibility(View.VISIBLE);
        videoView_four_layout.setVisibility(View.VISIBLE);
    }


    private void hideDefaultControls(){
        videoView_go_back.setVisibility(View.GONE);
        videoView_one_layout.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
        imageView8.setVisibility(View.GONE);
        videoView_three_layout.setVisibility(View.GONE);
        videoView_four_layout.setVisibility(View.GONE);
        //Todo this function will hide status and navigation when we click on screen

    }



    @Override
    protected void onResume() {
        super.onResume();
        sp.edit().putInt("currentPage"+ NameAndEpesoed, (int) simpleExoPlayer.getCurrentPosition()).apply();
        sp.edit().putInt("allduration"+ NameAndEpesoed, (int) simpleExoPlayer.getDuration()).apply();

        if (!message.equals("local")){
            if(simpleExoPlayer.getDuration() > 6000){
                SaveinDB();
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
        sp.edit().putInt("currentPage"+ NameAndEpesoed, (int) simpleExoPlayer.getCurrentPosition()).apply();
        sp.edit().putInt("allduration"+ NameAndEpesoed, (int) simpleExoPlayer.getDuration()).apply();
        if (!message.equals("local")){
            if(simpleExoPlayer.getDuration() > 6000){
                SaveinDB();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        super.onPause();
        if (simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
            mLastPosition = simpleExoPlayer.getCurrentPosition();
        }
        sp.edit().putInt("currentPage"+ NameAndEpesoed, (int) simpleExoPlayer.getCurrentPosition()).apply();
        sp.edit().putInt("allduration"+ NameAndEpesoed, (int) simpleExoPlayer.getDuration()).apply();
        if (!message.equals("local")){
            if(simpleExoPlayer.getDuration() > 6000){
                SaveinDB();
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        simpleExoPlayer.stop();
        sp.edit().putInt("currentPage"+ NameAndEpesoed, (int) simpleExoPlayer.getCurrentPosition()).apply();
        sp.edit().putInt("allduration"+ NameAndEpesoed, (int) simpleExoPlayer.getDuration()).apply();

        if (!message.equals("local")){
            if(simpleExoPlayer.getDuration() > 6000){
                SaveinDB();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    public class myepesoedSeasonsAdapter extends RecyclerView.Adapter<myepesoedSeasonsAdapter.myviewholder> {

        List<ServerModel> chaptermodelList;
        SeasonModel seasonModel;
        Context context;
        String title,seasonname;
        JseriesDB comicsdb;
        private SharedPreferences sp;
        public myepesoedSeasonsAdapter(List<ServerModel> chaptermodelList, Context context, String title, String seasonname,SeasonModel seasonModel) {
            this.chaptermodelList = chaptermodelList;
            this.context = context;
            this.title = title;
            this.seasonname = seasonname;
            this.seasonModel = seasonModel;
            comicsdb = new JseriesDB(context);
            sp = context.getSharedPreferences(config.SETTINGS_NAME, MODE_PRIVATE);
        }

        @NonNull
        @Override
        public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item, parent, false);
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
            String epetitle = c.getLebel();




            holder.titleof.setText(title + " " + seasonname+":"+c.getLebel());


            Glide.with(holder.itemView.getContext()).load(tvmodel.getPoster()).into(holder.thumbnail);


            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (c.getSource().equals("Mp4") || c.getSource().equals("M3u8")){


                        SetEpeLinks(c.getUrl(),tvmodel.getTitle(),c.getLebel(),message);
                        allepisodeslayout.setVisibility(View.GONE);




                    }else if(c.getSource().equals("Embed")  || c.getSource().equals("StreamSB")  || c.getSource().equals("StreamTape")  || c.getSource().equals("DoodStream")){

                        EasyPlexSupportedHosts easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);
                        easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted( ) {
                            @Override
                            public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> arrayList, boolean b) {
                                Log.d("TAG", "onTaskCompleted: "+arrayList);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                        easyPlexSupportedHosts.find(c.getUrl());


                        startActivity(new Intent(context, WebPlayer.class)
                                .putExtra("link",c.getUrl()));




                    } else  {


                        Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), EpesodTAFASIL.class);
                        sendDataToDetailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        sendDataToDetailsActivity.putExtra("position",String.valueOf(gg));

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

            TextView titleof;
            ImageView thumbnail;

            public myviewholder(@NonNull View itemView) {
                super(itemView);

                titleof = itemView.findViewById(R.id.cartoontitle);
                thumbnail = itemView.findViewById(R.id.episode_cover);




            }


        }
    }

    private void OpenDialogWatch(SerieModel tvmodel, ServerModel c, SeasonModel seasonModel, int gg) {

        final Dialog dialog = new Dialog(JSPLAYER.this, R.style.MyAlertDialogTheme);
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
            }
        });
        dialog.findViewById(R.id.vlc).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                openWithVLC(c.getUrl(),tvmodel.getTitle() + " " + seasonModel.getName() + " " +c.getLebel(),tvmodel.getPoster());

            }
        });
        dialog.findViewById(R.id.mxPlayer).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                openWithMXPlayer(c.getUrl());

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
            JSPLAYER.this.startActivity(shareVideo);
        } catch (ActivityNotFoundException ex) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            String uriString = VLC_INTENT ;
            intent.setData(Uri.parse(uriString));
            JSPLAYER.this.startActivity(intent);
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