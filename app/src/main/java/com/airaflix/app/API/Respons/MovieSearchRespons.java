package com.airaflix.app.API.Respons;

import com.airaflix.app.Models.SerieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSearchRespons {


    @SerializedName("data")
    @Expose()
    private List<SerieModel> movis;



    public List<SerieModel> getMovies(){
         return movis;

    }


}
