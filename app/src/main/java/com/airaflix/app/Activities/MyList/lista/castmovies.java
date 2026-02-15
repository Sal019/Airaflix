package com.airaflix.app.Activities.MyList.lista;


import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;


public class castmovies extends Fragment {

    private static final String CASTID = "castid";

    public static Fragment getintentt(@Nullable String castid) {
        Bundle args = new Bundle(1);
        args.putString(CASTID, castid);


        castmovies fragment = new castmovies();
        fragment.setArguments(args);
        return fragment;
    }

    ProgressBar progreed;
    RecyclerView rectmovies;
    SeriesAdapter recomAdapters;
    List<SerieModel> serieModel;
    FirebaseApp secondary;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_castmovies, container, false);

        rectmovies = view.findViewById(R.id.rectmovies);
        progreed = view.findViewById(R.id.progreed);

        String cast_id = getArguments().getString(CASTID);

        GetData(cast_id);
        return view;
    }

    private void GetData(String cast_id) {

        serieModel = new ArrayList<>();

        GridLayoutManager topadventers = new GridLayoutManager(getActivity(),3);
        rectmovies.setLayoutManager(topadventers);


        progreed.setVisibility(View.VISIBLE);
        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serieModel = new ArrayList<>();
                progreed.setVisibility(View.GONE);
                for (DataSnapshot ss:dataSnapshot.getChildren()) {
                    SerieModel sr = ss.getValue(SerieModel.class);
                    if (sr.getCast().equals(cast_id) && sr.getPlace().contains("movie")){
                        serieModel.add(sr);
                    }

                }
                recomAdapters = new SeriesAdapter(getActivity(),serieModel,0);
                rectmovies.setAdapter(recomAdapters);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}