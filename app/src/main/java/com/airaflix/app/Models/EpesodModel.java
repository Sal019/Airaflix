package com.airaflix.app.Models;

import com.google.firebase.database.Exclude;

public class EpesodModel {



     @Exclude
     String id;

     String server1;
     String server2;
     String epetitle;
     String postkey;
     String server3;

     public Boolean IsSelected = false;

    public Boolean getSelected() {
        return IsSelected;
    }

    public void setSelected(Boolean selected) {
        IsSelected = selected;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getServer1() {
        return server1;
    }

    public void setServer1(String downloadfhd) {
        this.server1 = downloadfhd;
    }

    public String getServer2() {
        return server2;
    }

    public void setServer2(String server2) {
        this.server2 = server2;
    }

    public String getEpetitle() {
        return epetitle;
    }

    public void setEpetitle(String epetitle) {
        this.epetitle = epetitle;
    }



    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }

    public String getServer3() {
        return server3;
    }

    public void setServer3(String server3) {
        this.server3 = server3;
    }


}
