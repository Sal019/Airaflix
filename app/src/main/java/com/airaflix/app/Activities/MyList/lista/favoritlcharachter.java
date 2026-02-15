package com.airaflix.app.Activities.MyList.lista;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.airaflix.app.Adapters.CastAdapter;
import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.Models.Cast;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class favoritlcharachter extends Fragment {
    ProgressBar progreed;
    RecyclerView rectmovies;
    Boolean reversed = false;
    String myID;
    CastAdapter castAdapter;
    List<Cast> castModel;
    FloatingActionButton filtercartoon;
    FirebaseFirestore mFirebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.favoritlcharachter, container, false);

        rectmovies = view.findViewById(R.id.rectmovies);
        progreed = view.findViewById(R.id.progreed);
        filtercartoon = view.findViewById(R.id.filtercartoon);

        myID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        GetData();


        return view;
    }


    private void GetData() {

        GridLayoutManager topadventers = new GridLayoutManager(getActivity(),3);
        rectmovies.setLayoutManager(topadventers);


        castModel = new ArrayList<>();





    }
}