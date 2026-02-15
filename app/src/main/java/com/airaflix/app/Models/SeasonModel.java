package com.airaflix.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeasonModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("movie_id")
    @Expose
    private String movieId;
    @SerializedName("Episodes")
    @Expose
    private String episodes;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;


    public SeasonModel() {
    }

    protected SeasonModel(Parcel in) {
        if (in.readByte( ) == 0) {
            id = null;
        } else {
            id = in.readInt( );
        }
        name = in.readString( );
        movieId = in.readString( );
        episodes = in.readString( );
        status = in.readString( );
        createdAt = in.readString( );
        updatedAt = in.readString( );
    }

    public static final Creator<SeasonModel> CREATOR = new Creator<SeasonModel>( ) {
        @Override
        public SeasonModel createFromParcel(Parcel in) {
            return new SeasonModel(in);
        }

        @Override
        public SeasonModel[] newArray(int size) {
            return new SeasonModel[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(movieId);
        dest.writeString(episodes);
        dest.writeString(status);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }
}
