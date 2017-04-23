package com.example.pc.myapplication;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.example.pc.myapplication.adapters.AtraccionTabAdapter;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.dialogs.AlertDialog;

public class AtraccionActivity extends AppCompatActivity{

    private AtraccionTabAdapter atraccionTabAdp;
    private TripTP tripTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_atraccion);

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
        if (!tripTP.isBanned() && tripTP.isLogin()) {
            atraccionTabAdp.makeComment();
        } else {
            if (tripTP.isBanned())
                AlertDialog.show(this,R.string.banned_alert);
            else if (!tripTP.isLogin()) {
                AlertDialog.show(this,R.string.login_alert);

            }
        }
    }
}
