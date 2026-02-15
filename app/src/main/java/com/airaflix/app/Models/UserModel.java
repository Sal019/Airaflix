package com.airaflix.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel implements Parcelable {


    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("name")
    @Expose
    String name;

    String email;
    String password;
    String Subscription;
    String profil;
    String Token;

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        id = in.readInt( );
        name = in.readString( );
        email = in.readString( );
        password = in.readString( );
        Subscription = in.readString( );
        profil = in.readString( );
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>( ) {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public int getUser_id() {
        return id;
    }

    public void setUser_id(int user_id) {
        this.id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubscription() {
        return Subscription;
    }

    public void setSubscription(String cover) {
        this.Subscription = cover;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String image) {
        this.profil = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(Subscription);
        dest.writeString(profil);
    }
}

