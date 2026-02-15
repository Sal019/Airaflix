package com.airaflix.app.API;


import com.airaflix.app.API.Respons.CategoriesResponse;
import com.airaflix.app.API.Respons.LoginResponse;
import com.airaflix.app.API.Respons.MovieCastRespons;
import com.airaflix.app.API.Respons.MovieSearchRespons;
import com.airaflix.app.Models.Cast2Model;
import com.airaflix.app.Models.SerieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {


    @GET("movie")
    Call<MovieSearchRespons> getmovies(
            @Query("page")    int page
    );

    @GET("search/movie/{search}")
    Call<MovieSearchRespons> searchmo(
            @Query("search")    String query
    );

    @GET("movie/{id}?")
    Call<SerieModel> getMovieDetiels(
            @Path("id")    int id
    );

    @GET("allmovietv")
    Call<MovieSearchRespons> getAllMoviesAndTVs();

    @GET("TrendingMovie")
    Call<MovieSearchRespons> getTrendingMovie();

    @GET("actionmovie")
    Call<MovieSearchRespons> getactionmovie(
    );


    @GET("supreheromovie")
    Call<MovieSearchRespons> getWatchedMoviesAndSeries(
    );


    @GET("getgeners")
    Call<CategoriesResponse> getgeners(
    );

    @POST("login")
    Call<LoginResponse> login(
            @Query("email")    String email,
            @Query("password")    String password

    );

    @POST("createacc")
    Call<LoginResponse> createacc(
            @Query("email")    String email,
            @Query("password")    String password,
            @Query("Subscription")    String Subscription,
            @Query("profil")    String profil,
            @Query("name")    String name

    );

    // <<<<---------------------- TV SHOW -------------------->>>

    @GET("tv/{tv_id}?")
    Call<SerieModel> getTVShowDetiels(
            @Path("tv_id")    int tv_id,
            @Query("api_key")    String api_key
    );
    @GET("tv")
    Call<MovieSearchRespons> getTVSHOWs(
            @Query("page")    int page
    );

    @GET("tv/popular")
    Call<MovieSearchRespons> getTVShowpopular(
            @Query("api_key")    String api_key,
            @Query("page")    int page
    );
    @GET("tv/top_rated")
    Call<MovieSearchRespons> getTVShowtop_rated(
            @Query("api_key")    String api_key,
            @Query("page")    int page
    );

    @GET("search/tv")
    Call<MovieSearchRespons> searchtv(
            @Query("api_key")    String api_key,
            @Query("query")    String query,
            @Query("page")    int page,
            @Query("include_adult")    Boolean include_adult
    );






}
