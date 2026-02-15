package com.airaflix.app.Activities.MyList.lista;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.DB.JseriesDB;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class favoritSeries extends Fragment {


    ProgressBar progreed;
    RecyclerView rectmovies;
    SeriesAdapter cartoonAdapter;
    List<SerieModel> serieModel;
    Boolean reversed = false;
    String myID;
    FirebaseApp secondary;
    FirebaseFirestore mFirebaseFirestore;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.favoritlcartoon, container, false);
        rectmovies = view.findViewById(R.id.rectmovies);
        progreed = view.findViewById(R.id.progreed);


        GetData();




        return view;
    }



    private void GetData() {

        GridLayoutManager topadventers = new GridLayoutManager(getActivity(),3);
        rectmovies.setLayoutManager(topadventers);


        serieModel = new ArrayList<>();


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

            if (!comicsModel.getPlace().contains("movie")){
                serieModel.add(comicsModel);
            }


        }
        progreed.setVisibility(View.GONE);
        cartoonAdapter = new SeriesAdapter(getActivity(), serieModel,0);
        rectmovies.setAdapter(cartoonAdapter);

    }
}