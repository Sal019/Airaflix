package com.airaflix.app.Fragments;

import static com.airaflix.app.Activities.MyList.lista.favoritlmovie.allseriesandmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airaflix.app.API.ModelView.MovieListViewModel;
import com.airaflix.app.API.MovieApi;
import com.airaflix.app.API.Respons.MovieSearchRespons;
import com.airaflix.app.API.Servicy;
import com.airaflix.app.Activities.ShowInfoActivity;
import com.airaflix.app.Adapters.MovieAdapter;
import com.airaflix.app.Adapters.SeriesAdapter;
import com.airaflix.app.Adapters.SliderAdapter;
import com.airaflix.app.Config.SwipyRefreshLayout;
import com.airaflix.app.Config.SwipyRefreshLayoutDirection;
import com.airaflix.app.Config.Utils;
import com.airaflix.app.Models.SerieModel;
import com.airaflix.app.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class movies extends Fragment {

    GridView recentepisod_rv;
    View  no_episode;
    MovieAdapter moviesAdapter;
    List<SerieModel> moviemodel;
    ShimmerFrameLayout shimmmer;

    MovieListViewModel movieListViewModel;

    MovieApi movieApi;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        recentepisod_rv = view.findViewById(R.id.recentepisod_rv);
        no_episode = view.findViewById(R.id.no_episode);
        shimmmer = view.findViewById(R.id.shimmmer);
        no_episode.setVisibility(View.GONE);
        movieApi = Servicy.getMovieApi();
        movieListViewModel = new ViewModelProvider(getActivity()).get(MovieListViewModel.class);
        movieListViewModel.MoviesApi(1);
        observAnyChanges();



        Utils.FixColumnsWidth(recentepisod_rv, 120, getContext());
        return view;
    }

    private void observAnyChanges() {

        movieListViewModel.getAllMovies().observe(getActivity( ), new Observer<List<SerieModel>>( ) {
            @Override
            public void onChanged(List<SerieModel> serieModels) {
                if (serieModels != null){
                    moviemodel = new ArrayList<>(  );
                    shimmmer.stopShimmer();
                    shimmmer.setVisibility(View.GONE);
                    moviesAdapter = new MovieAdapter(getActivity());

                    for (SerieModel s: serieModels) {
                        if (s.getPlace().contains("movie")){
                            moviemodel.add(s);
                        }
                    }
                    moviesAdapter.setmodels(moviemodel);


                    recentepisod_rv.setAdapter(moviesAdapter);
                    recentepisod_rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                         /*   startActivity(new Intent(getActivity(), DetailPage.class).putExtra("img",dramaModels.get(position).getImg())
                                    .putExtra("title",dramaModels.get(position).getTitle())
                                    .putExtra("link",dramaModels.get(position).getPageLink()));*/

                            SerieModel w = moviemodel.get(position);


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
                    no_episode.setVisibility(View.VISIBLE);

                }

            }
        });

      /*  recentepisod_rv.setOnScrollListener(new AbsListView.OnScrollListener( ) {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!view.canScrollHorizontally(1)){
                    movieListViewModel.MoviesNextPage();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
*/

    }

    private void GetRecentDramaEpisode() {


        Call<MovieSearchRespons> responsCall = movieApi
                .getmovies(
                        2
                );

        responsCall.enqueue(new Callback<MovieSearchRespons>( ) {
            @Override
            public void onResponse(Call<MovieSearchRespons> call, Response<MovieSearchRespons> response) {
                if (response.code() == 200){
                    List<SerieModel> movies = new ArrayList<>(response.body().getMovies());

                }



            }

            @Override
            public void onFailure(Call<MovieSearchRespons> call, Throwable t) {
                Log.d("ANASS TAGS", "onResponse: "+ t.getMessage());
            }
        });

    }

  /*  private void GetSlider() {


        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pogsslid.setVisibility(View.GONE);
                slider = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);
                    if (comicsm.getPlace().contains("movie")) {
                        slider.add(comicsm);
                    }
                }

                Collections.shuffle(slider);
                sliderAdapter = new SliderAdapter(getContext(),slider);
                sliderView.setSliderAdapter(sliderAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void GetData() {

        rectmovies.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rectmovies.setNestedScrollingEnabled(false);

        allseriesandmovies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progreed.setVisibility(View.GONE);
                Cartoonssearch = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    SerieModel comicsm = data.getValue(SerieModel.class);

                    if (comicsm.getPlace().contains("movie")){
                        Cartoonssearch.add(comicsm);
                    }



                }

                Collections.reverse(Cartoonssearch);


                rectmovies.setAdapter(new SeriesAdapter(getActivity(),Cartoonssearch,0));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void renewItem(SliderView sliderView) {

        slider = new ArrayList<>();
        SerieModel dataItems = new SerieModel();
        slider.add(dataItems);

        sliderAdapter.renewItem(slider);
        sliderAdapter.deleteItems(0);

    }*/
}