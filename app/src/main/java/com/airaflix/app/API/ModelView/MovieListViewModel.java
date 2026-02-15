package com.airaflix.app.API.ModelView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.airaflix.app.API.repositories.MovieRepository;
import com.airaflix.app.Models.CatigoriesModel;
import com.airaflix.app.Models.SerieModel;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    MovieRepository movieRepository;

    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }


    //------------------------ models ------------------------------------//


    public LiveData<List<SerieModel>> getAllMovies() {
        return movieRepository.getAllMovies();
    }
    public LiveData<List<SerieModel>> gettvs() {
        return movieRepository.geTALLTVs();
    }
    public LiveData<List<SerieModel>> gettrendm() {
        return movieRepository.geTTrends();
    }
    public LiveData<List<SerieModel>> getmAction() {
        return movieRepository.getmAction();
    }
    public LiveData<List<SerieModel>> getmswatched() {
        return movieRepository.getmswatched();
    }
public LiveData<List<CatigoriesModel>> GenersModel() {
        return movieRepository.GenersModel();
    }






    //------------------------ CALLS Movies ------------------------------------//


    public void MoviesApi(int page){
        movieRepository.Movies(page);
    }
    public void MoviesNextPage(){
        movieRepository.NextPageMovies();
    }

    public void TVsApi(int page){
        movieRepository.TVs(page);
    }
    public void trendmApi(int page){
        movieRepository.trendm(page);
    }
    public void GetActionMovie(int page){
        movieRepository.GetActionMovie(page);
    }
    public void GetWatchedMoviesAndSeries(int page){
        movieRepository.GetWatchedMoviesAndSeries(page);
    }
    public void GetGeners(int page){
        movieRepository.GetGeners(page);
    }







    //------------------------ CALLS TVShowsS ------------------------------------//







    //------------------------ Search ------------------------------------//

    public void setQuery(String queryData)
    {
        movieRepository.SearchTeext(queryData);
    }

    public LiveData<String> getquery() {
        return movieRepository.getSearch();
    }

}
