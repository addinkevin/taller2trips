package com.example.pc.myapplication;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.adapters.AtraccionTabAdapter;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.services.CollectionService;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AtraccionActivity extends AppCompatActivity{

    private AtraccionTabAdapter atraccionTabAdp;
    private TripTP tripTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle((R.string.atraccion));
        setSupportActionBar(toolbar);
        tripTP = (TripTP) getApplication();

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        atraccionTabAdp = new AtraccionTabAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(atraccionTabAdp);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onBackPressed() {
        atraccionTabAdp.onBackPressed();
        super.onBackPressed();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        atraccionTabAdp.onRestoreInstanceState(savedInstanceState);
    }


    public void showAudioController(View view) {
        atraccionTabAdp.showAudioController();
    }

    public void audioClick(View view) {
        atraccionTabAdp.audioClick();
    }

    public void makeComment(View view) {
        if (tripTP.isLogin()) {
            atraccionTabAdp.makeComment();
        } else {
            AlertDialog.show(this,R.string.login_alert);
        }
    }

}
