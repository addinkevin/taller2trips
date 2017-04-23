package com.example.pc.myapplication.application;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.pc.myapplication.commonfunctions.Consts;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class TripTP extends Application {

    private String url = null;
    private Integer radio = -1;
    private String splexSecret;
    private String socialDef = null;
    private int socialDefID;
    private String userID_fromSocial = null;
    private boolean banned = false;
    private boolean login = false;
    private String userID_fromServ = null;
    private boolean hasMultipleAccounts;

    private static final String TWITTER_KEY = "SImsg2WXUTa6XGUxmr678Jtro ";
    private static final String TWITTER_SECRET = "BSlApiUVD8wOyoBuWzFS8wv31leVCUQ5XlA2Z7sdXY57XkG3wd";

    public void onCreate() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        hasMultipleAccounts = false;
        super.onCreate();
    }

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

    public String getSocialDef() {
        if (socialDef == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            this.socialDef = sharedPreferences.getString(Consts.SOCIAL_DEF, "");
        }
        return socialDef;
    }

    public void setSocialDef(String socialDef) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Consts.SOCIAL_DEF, socialDef);
        editor.apply();
        this.socialDef = socialDef;
    }

    public int getSocialDefID() {
        return socialDefID;
    }

    public void setSocialDefID(int socialDefID) {
        this.socialDefID = socialDefID;
    }

    public String getSplexSecret() {
   /*     if (url == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            this.url = sharedPreferences.getString(Consts.URL, "");
        }*/
        return splexSecret;
    }

    public void setSplexSecret(String accToken) {
       /* SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Consts.URL, newUrl);
        editor.apply();*/
        this.splexSecret = accToken;
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

    public String getUserID_fromSocial() {
        return userID_fromSocial;
    }

    public void setUserID_fromSocial(String userID_fromSocial) {
        this.userID_fromSocial = userID_fromSocial;
    }

    public String getUserID_fromServ() {
        return userID_fromServ;
    }

    public void setUserID_fromServ(String userID_fromServ) {
        this.userID_fromServ = userID_fromServ;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean hasMultipleAccounts() {
        return hasMultipleAccounts;
    }

    public void setHasMultipleAccounts(boolean hasMultipleAccounts) {
        this.hasMultipleAccounts = hasMultipleAccounts;
    }
}