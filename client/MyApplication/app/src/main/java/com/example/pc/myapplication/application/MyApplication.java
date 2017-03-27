package com.example.pc.myapplication.application;


import android.app.Application;

public class MyApplication extends Application {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String newUrl) {
        this.url = newUrl;
    }

}