package com.airaflix.app.Activities.ShowList;

import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airaflix.app.API.MovieApi;
import com.airaflix.app.API.Respons.MovieSearchRespons;
import com.airaflix.app.API.Servicy;
import com.airaflix.app.Activities.ShowInfoActivity;
import com.airaflix.app.Adapters.MovieAdapter;
import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.Config.Utils;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class morelikeit extends Fragment {

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

        morelikeit fragment = new morelikeit();
        fragment.setArguments(args);
        return fragment;
    }

    public morelikeit() {
        // Required empty public constructor
    }

    GridView recentepisod_rv;
    MovieAdapter moviesAdapter;

    ProgressBar progreed;
    TextView shownose;
    MovieApi movieApi;

    String posterid,title,id,poste,place,age,studio,date,desc,cast,tasnif,where,mortabit_id,chapters;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_morelikeit, container, false);
        progreed = view.findViewById(R.id.progreed);
        recentepisod_rv = view.findViewById(R.id.recentepisod_rv);
        shownose = view.findViewById(R.id.shownose);
        movieApi = Servicy.getMovieApi();

        InitiaStrings();
        Utils.FixColumnsWidth(recentepisod_rv, 120, getContext());

        CheckifThereSeaons(posterid,tasnif,place);
        return view;
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
    List<SerieModel> serieModelList;
    private void CheckifThereSeaons(String postkey, String tasnif, String place) {


        Call<MovieSearchRespons> responsCall = movieApi
                .getAllMoviesAndTVs();


        responsCall.enqueue(new Callback<MovieSearchRespons>( ) {
            @Override
            public void onResponse(Call<MovieSearchRespons> call, Response<MovieSearchRespons> response) {

                List<SerieModel> Series = new ArrayList<>(response.body().getMovies());
                serieModelList = new ArrayList<>(  );
                for (SerieModel sp : Series) {

                    if (sp.getGener().contains(tasnif) && !sp.getTmdb_id().equals(postkey) && !sp.getPlace().contains("Published") ){
                        serieModelList.add(sp);
                    }
                }

                if (serieModelList.size() > 0){
                    progreed.setVisibility(View.GONE);
                    moviesAdapter = new MovieAdapter(getActivity());
                    moviesAdapter.setmodels(serieModelList);

                    recentepisod_rv.setAdapter(moviesAdapter);
                    recentepisod_rv.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            SerieModel w = serieModelList.get(position);

                            Intent sendDataToDetailsActivity = new Intent(getActivity(), ShowInfoActivity.class);
                            sendDataToDetailsActivity.putExtra("title",w.getTitle());
                            sendDataToDetailsActivity.putExtra("id",w.getId());
                            sendDataToDetailsActivity.putExtra("cover",w.getPoster());
                            sendDataToDetailsActivity.putExtra("poste",w.getPoster());
                            sendDataToDetailsActivity.putExtra("link",w.getLink_id());
                            sendDataToDetailsActivity.putExtra("postkey",w.getTmdb_id());
                            sendDataToDetailsActivity.putExtra("age",w.getAge());
                            sendDataToDetailsActivity.putExtra("studio",w.getCountry());
                            sendDataToDetailsActivity.putExtra("date",w.getYear());
                            sendDataToDetailsActivity.putExtra("desc",w.getStory());
                            sendDataToDetailsActivity.putExtra("cast",w.getCast());
                            sendDataToDetailsActivity.putExtra("mortabit",w.getOther_season_id());
                            sendDataToDetailsActivity.putExtra("place",w.getPlace());
                            sendDataToDetailsActivity.putExtra("tasnif",w.getGener());
                            sendDataToDetailsActivity.putExtra("views",w.getViews());
                            sendDataToDetailsActivity.putExtra("youtubeid",w.getUpdated_at());
                            sendDataToDetailsActivity.putExtra("isintent","1");

                          startActivity(sendDataToDetailsActivity);
                        }
                    });

                }else {
                    progreed.setVisibility(View.GONE);

                    shownose.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onFailure(Call<MovieSearchRespons> call, Throwable t) {

            }
        });



/*
        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serieModelList = new ArrayList<>();
                progreed.setVisibility(View.GONE);
                for (DataSnapshot ss:snapshot.getChildren()) {
                    SerieModel sp = ss.getValue(SerieModel.class);

                    if (sp.getGener().equals(tasnif) && !sp.getTmdb_id().equals(postkey) ){
                        serieModelList.add(sp);
                    }
                }

                if (serieModelList.size() > 0){
                    otherseason_rv.setAdapter(new SeriesAdapter(getActivity(),serieModelList,0));
                }else {
                    shownose.setVisibility(View.VISIBLE);
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

    }
}