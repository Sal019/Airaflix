package com.airaflix.app.API.Respons;

import com.airaflix.app.Models.CatigoriesModel;
import com.airaflix.app.Models.SerieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {

    @SerializedName("data")
    @Expose()
    private List<CatigoriesModel> getigories;


    public List<CatigoriesModel> getGetigories() {
        return getigories;
    }
}
