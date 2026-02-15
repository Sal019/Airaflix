package com.airaflix.app.Fragments;

import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.airaflix.app.API.ModelView.MovieListViewModel;
import com.airaflix.app.API.MovieApi;
import com.airaflix.app.API.Servicy;
import com.airaflix.app.Adapters.CategoriesAdapter;
import com.airaflix.app.Adapters.CountinuAdapter;
import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.Adapters.SliderAdapter;
import com.airaflix.app.Config.Function;
import com.airaflix.app.DB.JseriesDB;
import com.airaflix.app.MainActivity;
import com.airaflix.app.Models.CatigoriesModel;
import com.airaflix.app.Models.CountinuModel;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class home extends Fragment {


    //Database Firestore
    FirebaseFirestore mFirebaseFirestore;

    //Ads
    private StartAppAd startAppAd;



    //Slider Change
    SliderView sliderView;
    SliderAdapter sliderAdapter;
    List<SerieModel> slider;
    ProgressBar pogsslid;

    //Categories Change
    RecyclerView catigo_rv;
    CategoriesAdapter categoriesAdapter;
    List<CatigoriesModel> catigoriesModels;
    ProgressBar pogstud;

    View statusBar;

    //MyList
    RecyclerView pin_rv;
    SeriesAdapter mylistadapter;
    List<SerieModel> mylistmodel;
    ProgressBar progpin;
    RelativeLayout pin_rlt;


    //Trende
    RecyclerView trend_rv;
    SeriesAdapter trendadapter;
    List<SerieModel> trendmodel;
    ProgressBar progmoswt;


    //Anime
    RecyclerView anime_rv;
    SeriesAdapter animeadapter;
    List<SerieModel> animemodel;
    ProgressBar progmoswtco;

    //Top Movie
    RecyclerView topmovie_rv;
    SeriesAdapter topmovieadapter;
    List<SerieModel> topmoviemodel;
    ProgressBar progmoswtmovi;


    //Top series
    RecyclerView seriewatched_rv;
    SeriesAdapter seriewatchedadapter;
    List<SerieModel> seriewatchedmodel;
    ProgressBar proganew;


    //Thhis Year
    RecyclerView newyera_rv;
    SeriesAdapter newadapter;
    List<SerieModel> newmodel;
    ProgressBar progayewar;

    //Countinu watch
    RecyclerView count_rv;
    CountinuAdapter countinuAdapter;
    List<CountinuModel> countinuModels;
    ProgressBar progcont;
    RelativeLayout continowach;


    MovieListViewModel movieListViewModel;

    MovieApi movieApi;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).hideStatusBar();
        initial(view);


        observAnyChanges();
        GetSlider();
        GetCategories();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            pin_rlt.setVisibility(View.VISIBLE);
            GetMyList();
        }else{
            pin_rlt.setVisibility(View.GONE);
        }
        getTrends();
        getAnimes();
        getTopMovies();
        getTopSeire();
        getThisYear();
        getCountinuWatching();


        return  view;
    }

    private void observAnyChanges() {

        movieListViewModel.gettrendm().observe(getActivity( ), new Observer<List<SerieModel>>( ) {
            @Override
            public void onChanged(List<SerieModel> serieModels) {
                if (serieModels != null){

                    trend_rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

                    progmoswt.setVisibility(View.GONE);
                    trendadapter = new SeriesAdapter(getContext(),serieModels,0);
                    trend_rv.setAdapter(trendadapter);
                }
            }
        });


    }

    private void initial(View view) {


        Banner banner = view.findViewById(R.id.startAppBanner);
        sliderView = view.findViewById(R.id.slider);
        pogsslid = view.findViewById(R.id.pogsslid);
        pogstud = view.findViewById(R.id.pogstud);
        catigo_rv = view.findViewById(R.id.catigo_rv);
        progpin = view.findViewById(R.id.progpin);
        pin_rv = view.findViewById(R.id.pin_rv);
        pin_rlt = view.findViewById(R.id.pin_rlt);
        progmoswt = view.findViewById(R.id.progmoswt);
        trend_rv = view.findViewById(R.id.trend_rv);
        progmoswtco = view.findViewById(R.id.progmoswtco);
        anime_rv = view.findViewById(R.id.anime_rv);
        topmovie_rv = view.findViewById(R.id.topmovie_rv);
        progmoswtmovi = view.findViewById(R.id.progmoswtmovi);
        proganew = view.findViewById(R.id.proganew);
        seriewatched_rv = view.findViewById(R.id.seriewatched_rv);
        progayewar = view.findViewById(R.id.progayewar);
        newyera_rv = view.findViewById(R.id.newyera_rv);
        count_rv = view.findViewById(R.id.count_rv);
        progcont = view.findViewById(R.id.progcont);
        continowach = view.findViewById(R.id.continowach);

        movieApi = Servicy.getMovieApi();
        movieListViewModel = new ViewModelProvider(getActivity()).get(MovieListViewModel.class);
        movieListViewModel.trendmApi(1);
        movieListViewModel.MoviesApi(1);
        movieListViewModel.GetActionMovie(1);
        movieListViewModel.GetWatchedMoviesAndSeries(1);
        movieListViewModel.GetGeners(1);



        //Fixed
        banner.loadAd();


        sliderAdapter = new SliderAdapter(getContext(),slider);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        renewItem(sliderView);

        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getCountinuWatching() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        count_rv.setLayoutManager(linearLayoutManager);

        countinuModels = new ArrayList<>();

        Cursor cursor = new JseriesDB(getActivity()).GetCountinuDB();
        progcont.setVisibility(View.GONE);

        while (cursor.moveToNext()){
            CountinuModel comicsModel = new CountinuModel();
            comicsModel.setId(cursor.getString(0));
            comicsModel.setTitle(cursor.getString(1));
            comicsModel.setPoster(cursor.getString(3));
            comicsModel.setCover(cursor.getString(3));
            comicsModel.setYear(cursor.getString(4));
            comicsModel.setPlace(cursor.getString(10));
            comicsModel.setGener(cursor.getString(5));
            comicsModel.setTmdb_id(cursor.getString(11));
            comicsModel.setCountry(cursor.getString(6));
            comicsModel.setAge(cursor.getString(7));
            comicsModel.setStory(cursor.getString(8));
            comicsModel.setSeason_name(cursor.getString(19));
            comicsModel.setSeason_episodes(cursor.getString(12));
            comicsModel.setServer_lebel(cursor.getString(14));
            comicsModel.setServer_source(cursor.getString(20));
            comicsModel.setServer_url(cursor.getString(18));

           // comicsModel.setPosition(cursor.getString(1));
            comicsModel.setCurrent_position(cursor.getString(15));
            comicsModel.setFull_duration(cursor.getString(16));

            countinuModels.add(comicsModel);
        }



        if (countinuModels.size() <= 0){
            continowach.setVisibility(View.GONE);
        }

        countinuAdapter = new CountinuAdapter(countinuModels,getActivity());
        count_rv.setAdapter(countinuAdapter);
    }

    private void getThisYear() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);




        movieListViewModel.getAllMovies().observe(getActivity( ), new Observer<List<SerieModel>>( ) {
            @Override
            public void onChanged(List<SerieModel> serieModels) {
                if (serieModels != null){

                    newyera_rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

                    progayewar.setVisibility(View.GONE);

                    newmodel = new ArrayList<>();

                    for (SerieModel data : serieModels) {


                        if (Function.ToInterger(data.getYear()) == currentYear){
                            newmodel.add(data);
                        }


                    }


                    animeadapter = new SeriesAdapter(getContext(),newmodel,0);
                    newyera_rv.setAdapter(animeadapter);
                }
            }
        });

    }

    private void getTopSeire() {


        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);

        seriewatched_rv.setLayoutManager(ll);

        allseriesandmovies.limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                proganew.setVisibility(View.GONE);
                seriewatchedmodel = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);

                    if (comicsm.getPlace().contains("serie")){
                        seriewatchedmodel.add(comicsm);
                    }




                }

                seriewatchedadapter = new SeriesAdapter(getContext(),seriewatchedmodel,1);
                seriewatched_rv.setAdapter(seriewatchedadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTopMovies() {


        movieListViewModel.getmswatched().observe(getActivity( ), new Observer<List<SerieModel>>( ) {
            @Override
            public void onChanged(List<SerieModel> serieModels) {
                if (serieModels != null){

                    topmovie_rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

                    progmoswtmovi.setVisibility(View.GONE);
                    animeadapter = new SeriesAdapter(getContext(),serieModels,0);
                    topmovie_rv.setAdapter(animeadapter);
                }
            }
        });


    }

    private void getAnimes() {



        movieListViewModel.getmAction().observe(getActivity( ), new Observer<List<SerieModel>>( ) {
            @Override
            public void onChanged(List<SerieModel> serieModels) {
                if (serieModels != null){

                    anime_rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

                    progmoswtco.setVisibility(View.GONE);
                    animeadapter = new SeriesAdapter(getContext(),serieModels,0);
                    anime_rv.setAdapter(animeadapter);
                }
            }
        });

    }

    private void getTrends() {


    }

    private void GetMyList() {

        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(RecyclerView.HORIZONTAL);
        pin_rv.setLayoutManager(ll);
        mylistmodel = new ArrayList<>();


        Cursor cursor = new JseriesDB(getActivity()).GetMyListDB();

        while (cursor.moveToNext()){
            SerieModel comicsModel = new SerieModel();
            comicsModel.setId(cursor.getString(0));
            comicsModel.setTitle(cursor.getString(1));
            comicsModel.setTmdb_id(cursor.getString(2));
            comicsModel.setPoster(cursor.getString(3));
            comicsModel.setYear(cursor.getString(4));
            comicsModel.setGener(cursor.getString(5));
            comicsModel.setCountry(cursor.getString(6));
            comicsModel.setAge(cursor.getString(7));
            comicsModel.setStory(cursor.getString(8));
            comicsModel.setPlace(cursor.getString(9));
            comicsModel.setCast(cursor.getString(10));
            comicsModel.setOther_season_id(cursor.getString(11));
            comicsModel.setLink_id(cursor.getString(12));
            comicsModel.setViews_db(cursor.getString(13));
            comicsModel.setRating_db(cursor.getString(14));
            comicsModel.setCreated_at(cursor.getString(15));


            mylistmodel.add(comicsModel);
        }

        if (mylistmodel.size() > 0){
            progpin.setVisibility(View.GONE);
            mylistadapter = new SeriesAdapter(getActivity(), mylistmodel,0);
            pin_rv.setAdapter(mylistadapter);

        }else pin_rlt.setVisibility(View.GONE);


  /*      mFirebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                collection("mylist").
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            SerieModel sm = ds.toObject(SerieModel.class);
                            mylistmodel.add(sm);
                        }

                        if (mylistmodel.size() > 0){

                            mylistadapter = new SeriesAdapter(getActivity(), mylistmodel);
                            pin_rv.setAdapter(mylistadapter);

                        }else pin_rlt.setVisibility(View.GONE);




                    }
                });*/
                /*.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<SerieModel> downloadInfoList = task.getResult().toObjects(SerieModel.class);
                                for (SerieModel downloadInfo : downloadInfoList) {
                                    mylistmodel.add(downloadInfo);
                                }

                                mylistadapter = new SeriesAdapter(getActivity(), mylistmodel);
                                pin_rv.setAdapter(mylistadapter);
                            }else {

                            }
                        }

                    }
                });*/


    }

    private void GetCategories() {


        movieListViewModel.GenersModel().observe(getActivity( ), new Observer<List<CatigoriesModel>>( ) {
            @Override
            public void onChanged(List<CatigoriesModel> catigoriesModels) {
                if (catigoriesModels != null){
                    catigo_rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

                    pogstud.setVisibility(View.GONE);
                    categoriesAdapter = new CategoriesAdapter(getContext(),catigoriesModels);
                    catigo_rv.setAdapter(categoriesAdapter);
                }
            }
        });

    }


    private void GetSlider() {



        movieListViewModel.getAllMovies().observe(getActivity( ), new Observer<List<SerieModel>>( ) {
            @Override
            public void onChanged(List<SerieModel> serieModels) {
                if (serieModels != null){
                    slider = new ArrayList<>();

                    for (int i = 6; i <serieModels.size() ; i++) {
                        slider.add(serieModels.get(i));
                    }

                    pogsslid.setVisibility(View.GONE);
                    sliderAdapter = new SliderAdapter(getContext(),serieModels);
                    sliderView.setSliderAdapter(sliderAdapter);


                }
            }
        });
    }
    private void renewItem(SliderView sliderView) {

        slider = new ArrayList<>();
        SerieModel dataItems = new SerieModel();
        slider.add(dataItems);

        sliderAdapter.renewItem(slider);
        sliderAdapter.deleteItems(0);

    }


}