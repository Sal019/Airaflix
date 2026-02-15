package com.airaflix.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class  SerieModel implements Parcelable {

    public String id;
    private String title;
    private String poster;
    private String cover;
    private String year;
    private String place;
    private String gener;
    private String created_at;
    private String tmdb_id;
    private String country;
    private String age;
    private String story;
    private String other_season_id;
    private String cast;
    private String link_id;
    private long views;
    private String updated_at;
    private String views_db;
    private String rating_db;


    private ArrayList<ServerModel> videos;
    private ArrayList<SeasonModel> Seasons;
    //fixed
    public int type;

    public SerieModel() {
    }


    protected SerieModel(Parcel in) {
        id = in.readString( );
        title = in.readString( );
        poster = in.readString( );
        cover = in.readString( );
        year = in.readString( );
        place = in.readString( );
        gener = in.readString( );
        created_at = in.readString( );
        tmdb_id = in.readString( );
        country = in.readString( );
        age = in.readString( );
        story = in.readString( );
        other_season_id = in.readString( );
        cast = in.readString( );
        link_id = in.readString( );
        views = in.readLong( );
        updated_at = in.readString( );
        views_db = in.readString( );
        rating_db = in.readString( );
        type = in.readInt( );
    }

    public static final Creator<SerieModel> CREATOR = new Creator<SerieModel>( ) {
        @Override
        public SerieModel createFromParcel(Parcel in) {
            return new SerieModel(in);
        }

        @Override
        public SerieModel[] newArray(int size) {
            return new SerieModel[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setTmdb_id(String tmdb_id) {
        this.tmdb_id = tmdb_id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setOther_season_id(String other_season_id) {
        this.other_season_id = other_season_id;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setViews_db(String views_db) {
        this.views_db = views_db;
    }

    public void setRating_db(String rating_db) {
        this.rating_db = rating_db;
    }

    public void setVideos(ArrayList<ServerModel> videos) {
        this.videos = videos;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getCover() {
        return cover;
    }

    public String getYear() {
        return year;
    }

    public String getPlace() {
        return place;
    }

    public String getGener() {
        return gener;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getTmdb_id() {
        return tmdb_id;
    }

    public String getCountry() {
        return country;
    }

    public String getAge() {
        return age;
    }

    public String getStory() {
        return story;
    }

    public String getOther_season_id() {
        return other_season_id;
    }

    public String getCast() {
        return cast;
    }

    public String getLink_id() {
        return link_id;
    }

    public long getViews() {
        return views;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getViews_db() {
        return views_db;
    }

    public String getRating_db() {
        return rating_db;
    }

    public ArrayList<ServerModel> getVideos() {
        return videos;
    }

    public int getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<SeasonModel> getSeasons() {
        return Seasons;
    }

    public void setSeasons(ArrayList<SeasonModel> seasons) {
        Seasons = seasons;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(cover);
        dest.writeString(year);
        dest.writeString(place);
        dest.writeString(gener);
        dest.writeString(created_at);
        dest.writeString(tmdb_id);
        dest.writeString(country);
        dest.writeString(age);
        dest.writeString(story);
        dest.writeString(other_season_id);
        dest.writeString(cast);
        dest.writeString(link_id);
        dest.writeLong(views);
        dest.writeString(updated_at);
        dest.writeString(views_db);
        dest.writeString(rating_db);
        dest.writeInt(type);
    }
}
