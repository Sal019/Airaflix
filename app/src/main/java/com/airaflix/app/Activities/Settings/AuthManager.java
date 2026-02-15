package com.airaflix.app.Activities.Settings;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.airaflix.app.Models.UserModel;


public class AuthManager {


    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;


    public static final String PREMUIM = "PREMUIM";
    public static final String AUTH_NAME = "AUTH_NAME";
    public static final String AUTH_EMAIL = "AUTH_EMAIL";
    public static final String AUTH_ID = "AUTH_ID";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";



    @SuppressLint("CommitPrefEdits")
    public AuthManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public void saveSettings(UserModel userAuthInfo,String Token){

        editor.putString(PREMUIM, userAuthInfo.getSubscription()).commit();
        editor.putString(AUTH_NAME, userAuthInfo.getName()).commit();
        editor.putString(AUTH_EMAIL, userAuthInfo.getEmail()).commit();
        editor.putInt(AUTH_ID, userAuthInfo.getUser_id()).commit();
        editor.putString(AUTH_TOKEN, Token).commit();
        editor.apply();
    }



    public void deleteAuth(){
        editor.remove(PREMUIM).commit();
        editor.remove(AUTH_NAME).commit();
        editor.remove(AUTH_ID).commit();
        editor.remove(AUTH_TOKEN).commit();
        editor.remove(AUTH_EMAIL).commit();
    }

    public UserModel getUserInfo() {
        UserModel userAuthInfo = new UserModel();
        userAuthInfo.setSubscription(prefs.getString(PREMUIM, null));
        userAuthInfo.setName(prefs.getString(AUTH_NAME, null));
        userAuthInfo.setEmail(prefs.getString(AUTH_EMAIL, null));
        userAuthInfo.setUser_id(prefs.getInt(AUTH_ID, 0));
        userAuthInfo.setToken(prefs.getString(AUTH_TOKEN, null));
        return userAuthInfo;
    }


}
