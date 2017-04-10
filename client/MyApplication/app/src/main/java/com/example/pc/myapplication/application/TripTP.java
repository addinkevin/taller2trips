package com.example.pc.myapplication.application;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.pc.myapplication.commonfunctions.Consts;

public class TripTP extends Application {

    private String url;
    private Integer radio = -1;

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

    public Integer getRadio() {
        if (radio == -1) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            this.radio = sharedPreferences.getInt(Consts.RADIO, Consts.DEF_RADIO);
        }
        return radio;
    }

    public void setRadio(Integer radio) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Consts.RADIO, radio);
        editor.apply();
        this.radio = radio;
    }

}