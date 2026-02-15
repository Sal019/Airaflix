package com.airaflix.app.Activities;

import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airaflix.app.API.ModelView.MovieListViewModel;
import com.airaflix.app.API.MovieApi;
import com.airaflix.app.API.Servicy;
import com.airaflix.app.Adapters.MovieAdapter;
import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.Adapters.SliderAdapter;
import com.airaflix.app.Config.Utils;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Generpage extends AppCompatActivity {


    TextView studiotitle;
    GridView recentepisod_rv;
    String _gener;
    ProgressBar progreed;
    List<SerieModel> moviemodel;
    MovieAdapter moviesAdapter;
    MovieListViewModel movieListViewModel;

    MovieApi movieApi;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generpage);

        studiotitle = findViewById(R.id.studiotitle);
        recentepisod_rv = findViewById(R.id.recentepisod_rv);
        progreed = findViewById(R.id.progresssearch);
        movieApi = Servicy.getMovieApi();
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        movieListViewModel.MoviesApi(1);
        _gener = getIntent().getStringExtra("gener");
        studiotitle.setText(_gener);

        GetAllGeners(_gener);

        Utils.FixColumnsWidth(recentepisod_rv, 120, this);
    }


    private void GetAllGeners(String gener) {



        movieListViewModel.getAllMovies().observe(Generpage.this, new Observer<List<SerieModel>>( ) {
            @Override
            public void onChanged(List<SerieModel> serieModels) {
                if (serieModels != null){
                    moviemodel = new ArrayList<>(  );
                    moviesAdapter = new MovieAdapter(Generpage.this);
                    progreed.setVisibility(View.GONE);
                    for (SerieModel ss: serieModels) {
                        if (ss.getGener().contains(_gener)){
                            moviemodel.add(ss);

                        }

                    }
                    moviesAdapter.setmodels(moviemodel);
                    recentepisod_rv.setAdapter(moviesAdapter);
                    recentepisod_rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            SerieModel w = serieModels.get(position);


                            Intent sendDataToDetailsActivity = new Intent(Generpage.this, ShowInfoActivity.class);
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
                }

            }
        });





    }
}