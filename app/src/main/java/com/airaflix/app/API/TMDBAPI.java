package com.airaflix.app.API;

import static com.airaflix.app.API.BEANLINKS.BASEURL;

import com.airaflix.app.API.Respons.TMDB_API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBAPI {

    private static Retrofit.Builder retrofBuilder = new Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create());


    private static Retrofit retrofit = retrofBuilder.build();

    private static TMDB_API movieApi = retrofit.create(TMDB_API.class);

    public static TMDB_API getMovieApi(){
        return movieApi;
    }
}
