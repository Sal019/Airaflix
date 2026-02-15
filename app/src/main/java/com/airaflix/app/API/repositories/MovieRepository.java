package com.airaflix.app.API.repositories;

import androidx.lifecycle.LiveData;


import com.airaflix.app.API.MovieApiClient;
import com.airaflix.app.Models.CatigoriesModel;
import com.airaflix.app.Models.SerieModel;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;

    private static String Qerie;
    private static int MoviesPage,tvsPage;
    private static int nID;

    public static MovieRepository getInstance(){
        if (instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<SerieModel>> getSearcheMoives(){
        return movieApiClient.getSearchedMoives();
    }


    //-----------------Moives All ---------------//


    public LiveData<List<SerieModel>> getAllMovies(){
        return movieApiClient.getMoives();
    }

    public void Movies(int page){
        MoviesPage = page;
        movieApiClient.GetMoviesApi(page);
    }

    public void NextPageMovies(){
        movieApiClient.GetMoviesApi(MoviesPage+1);
    }


    public LiveData<List<SerieModel>> geTALLTVs(){
        return movieApiClient.getTVS();
    }

    public void TVs(int page){
        tvsPage = page;
        movieApiClient.GetTVsHOWSsApi(page);
    }



    public LiveData<List<SerieModel>> geTTrends(){
        return movieApiClient.getTrending();
    }

    public void trendm(int page){
        tvsPage = page;
        movieApiClient.GetTrendingApi(page);
    }


    public LiveData<List<SerieModel>> getmAction(){
        return movieApiClient.getmAction();
    }

    public void GetActionMovie(int page){
        tvsPage = page;
        movieApiClient.GetActionMovieApi(page);
    }

    public LiveData<List<SerieModel>> getmswatched(){
        return movieApiClient.getmswatched();
    }

    public void GetWatchedMoviesAndSeries(int page){
        tvsPage = page;
        movieApiClient.GetWatchedMoviesAndSeries(page);
    }

    public LiveData<List<CatigoriesModel>> GenersModel(){
        return movieApiClient.GenersModel();
    }

    public void GetGeners(int page){
        tvsPage = page;
        movieApiClient.GetGeners(page);
    }




    public LiveData<String> getSearch(){
        return movieApiClient.getSearchQue();
    }
    public void SearchTeext(String ss){
        movieApiClient.SetSearch(ss);
    }



}
