package com.example.pc.myapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.example.pc.myapplication.adapters.PuntoInteresTabAdapter;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.dialogs.AlertDialog;

public class PuntoInteresActivity extends AppCompatActivity {

    private TripTP tripTP;
    private PuntoInteresTabAdapter puntoInteresTabAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_punto_interes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle((R.string.atraccion));
        setSupportActionBar(toolbar);
        tripTP = (TripTP) getApplication();

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        puntoInteresTabAdp = new PuntoInteresTabAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(puntoInteresTabAdp);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        puntoInteresTabAdp.onBackPressed();
        super.onBackPressed();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        puntoInteresTabAdp.onRestoreInstanceState(savedInstanceState);
    }


    public void showAudioController(View view) {
        puntoInteresTabAdp.showAudioController();
    }

    public void audioClick(View view) {
        puntoInteresTabAdp.audioClick();
    }

    public void makeComment(View view) {
        if (tripTP.isLogin()) {
            puntoInteresTabAdp.makeComment();
        } else {
            AlertDialog.show(this,R.string.login_alert);
        }
    }
}
