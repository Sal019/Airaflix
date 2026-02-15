package com.airaflix.app.Activities.MyList.lista;



import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


public class castseries extends Fragment {

    private static final String CASTID = "castid";

    public static Fragment getintentt(@Nullable String castid) {
        Bundle args = new Bundle(1);
        args.putString(CASTID, castid);


        castseries fragment = new castseries();
        fragment.setArguments(args);
        return fragment;
    }

    ProgressBar progreed;
    RecyclerView rectmovies;
    SeriesAdapter recomAdapters;
    List<SerieModel> serieModel;
    FirebaseApp secondary;
    DatabaseReference fb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_castseries, container, false);
        rectmovies = view.findViewById(R.id.rectmovies);
        progreed = view.findViewById(R.id.progreed);


        String cast_id = getArguments().getString(CASTID);

        GetData(cast_id);

        return  view;
    }

    private void GetData(String cast_id) {

        GridLayoutManager topadventers = new GridLayoutManager(getActivity(),3);
        rectmovies.setLayoutManager(topadventers);
        rectmovies.setNestedScrollingEnabled(false);
        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progreed.setVisibility(View.GONE);
                serieModel = new ArrayList<>();
                for (DataSnapshot ss:snapshot.getChildren()) {
                    SerieModel sr = ss.getValue(SerieModel.class);
                    if (sr.getCast().contains(cast_id)){
                        serieModel.add(sr);
                    }

                }
                recomAdapters = new SeriesAdapter(getActivity(),serieModel,0);
                rectmovies.setAdapter(recomAdapters);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}