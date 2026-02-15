package com.airaflix.app.API.Respons;

import com.airaflix.app.Models.CastModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieCastRespons {

    @SerializedName("id")
    @Expose()
    private int id;

    @SerializedName("cast")
    @Expose()
    private List<CastModel> casts;



    public List<CastModel> getCasts() {
        return casts;
    }
}
