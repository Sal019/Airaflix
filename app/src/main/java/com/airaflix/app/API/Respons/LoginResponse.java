package com.airaflix.app.API.Respons;

import com.airaflix.app.Models.CatigoriesModel;
import com.airaflix.app.Models.UserModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {


    @SerializedName("user")
    @Expose()
    private UserModel getuser;

    @SerializedName("token")
    @Expose()
    private String Token;


    public UserModel getGetuser() {
        return getuser;
    }

    public String getToken() {
        return Token;
    }
}
