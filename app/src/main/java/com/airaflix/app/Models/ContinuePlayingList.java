package com.airaflix.app.Models;

public class ContinuePlayingList {

    int id;
    int contentID;
    String name;
    String year;
    String poster;
    String Url;
    long position;
    long duration;

    public ContinuePlayingList(int id, int contentID, String name, String year, String poster, String url, long position, long duration) {
        this.id = id;
        this.contentID = contentID;
        this.name = name;
        this.year = year;
        this.poster = poster;
        Url = url;
        this.position = position;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
