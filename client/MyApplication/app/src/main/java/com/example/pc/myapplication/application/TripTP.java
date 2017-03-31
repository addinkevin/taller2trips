package com.example.pc.myapplication.application;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.pc.myapplication.commonfunctions.Consts;

public class TripTP extends Application {

    private String url;

    public String getUrl() {
        if (url == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            this.url = sharedPreferences.getString(Consts.URL, "");
        }
        return url;
    }

    public void setUrl(String newUrl) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Consts.URL, newUrl);
        editor.apply();
        this.url = newUrl;
    }

}