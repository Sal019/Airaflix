package com.airaflix.app.Activities.ShowList;

import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.CastDb;
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
import android.widget.TextView;

import com.airaflix.app.Adapters.CastAdapter;
import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.Models.Cast;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class castinfo extends Fragment {


    private static final String KEY_CODE = "title";
    private static final String coverR = "cover";
    private static final String posteR = "poste";
    private static final String postkeyR = "postkey";
    private static final String ageR = "age";
    private static final String studioR = "studio";
    private static final String dateR = "date";
    private static final String descR = "desc";
    private static final String tasnifR = "tasnif";
    private static final String whereR = "where";
    private static final String castr = "cast";
    private static final String mortabit_idR = "mortabit_id";
    private static final String placeR = "place";
    private static final String chaptersR = "chapters";
    private static final String VIews = "views";

    @SuppressLint("SuspiciousIndentation")
    public static Fragment newInstance(@Nullable String code, String cover, String poste, String postkey,
                                       String age, String studio, String date, String desc, String cast, String tasnif,
                                       String where, String mortabit, String link, String place, String views) {
        Bundle args = new Bundle(13);
        if (code!= null)
            args.putString(KEY_CODE, code);
        args.putString(coverR, cover);
        args.putString(posteR, poste);
        args.putString(postkeyR, postkey);
        args.putString(ageR, age);
        args.putString(studioR, studio);
        args.putString(dateR, date);
        args.putString(descR, desc);
        args.putString(castr, cast);
        args.putString(tasnifR, tasnif);
        args.putString(whereR, where);
        args.putString(mortabit_idR, mortabit);
        args.putString(placeR, place);
        args.putString(chaptersR, link);
        args.putString(VIews, views);

        castinfo fragment = new castinfo();
        fragment.setArguments(args);
        return fragment;
    }

    public castinfo() {
        // Required empty public constructor
    }

    RecyclerView otherseason_rv;
    ProgressBar progreed;
    TextView shownose;

    String posterid,title,id,poste,place,age,studio,date,desc,cast,tasnif,where,mortabit_id,chapters;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_castinfo, container, false);

        progreed = view.findViewById(R.id.progreed);
        otherseason_rv = view.findViewById(R.id.otherseason_rv);
        shownose = view.findViewById(R.id.shownose);

        InitiaStrings();

        CheckifThereSeaons(cast);
        return  view;
    }

    private void InitiaStrings() {

        title = getArguments().getString(KEY_CODE);
        poste = getArguments().getString(posteR);
        chapters = getArguments().getString(chaptersR);
        posterid = getArguments().getString(postkeyR);
        place = getArguments().getString(placeR);
        age = getArguments().getString(ageR);
        studio = getArguments().getString(studioR);
        date = getArguments().getString(dateR);
        desc = getArguments().getString(descR);
        cast = getArguments().getString(castr);
        tasnif = getArguments().getString(tasnifR);
        where = getArguments().getString(whereR);
        mortabit_id = getArguments().getString(mortabit_idR);

    }
    List<Cast> castList;
    private void CheckifThereSeaons(String castid) {

        otherseason_rv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        otherseason_rv.setNestedScrollingEnabled(false);

        /*CastDb.child(castid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                castList = new ArrayList<>();
                progreed.setVisibility(View.GONE);
                for (DataSnapshot ss:snapshot.getChildren()) {
                    Cast sp = ss.getValue(Cast.class);
                    castList.add(sp);
                }

                if (castList.size() > 0){
                    otherseason_rv.setAdapter(new CastAdapter(getActivity(),castList));
                }else {
                    shownose.setVisibility(View.VISIBLE);
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }
}