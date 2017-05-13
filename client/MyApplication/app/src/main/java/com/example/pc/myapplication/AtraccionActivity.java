package com.example.pc.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.pc.myapplication.adapters.AtraccionTabAdapter;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;

import java.util.ArrayList;
import java.util.List;

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

        boolean isRecorrido = getIntent().getBooleanExtra(Consts.RECORRIDO, false);
        if (isRecorrido) {
            Atraccion nextAtr = null;
            Atraccion prevAtr = null;
            final int index = getIntent().getIntExtra(Consts.POS, -1);
            final List<Atraccion> atraccions = getIntent().getParcelableArrayListExtra(Consts.ATRACC);

            if (atraccions.size() < index + 1) {
                nextAtr = atraccions.get(index + 1);
            }

            if (index - 1 >= 0) {
                prevAtr = atraccions.get(index - 1);
            }

            FrameLayout nextAct = (FrameLayout) findViewById(R.id.nextAtr);
            if (nextAtr != null && index + 1 < atraccions.size()) {
                TextView nextNameV = (TextView) nextAct.findViewById(R.id.nextAtrName);
                nextNameV.setText(nextAtr.nombre);
                nextAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToAtraccion(atraccions, index + 1);
                    }
                });
            } else {
                nextAct.setVisibility(View.GONE);
            }

            FrameLayout prevAct = (FrameLayout) findViewById(R.id.prevAtr);
            TextView prevNameV = (TextView) prevAct.findViewById(R.id.prevAtrName);
            if (prevAtr != null) {
                prevNameV.setText(prevAtr.nombre);
            } else {
                prevNameV.setText(R.string.back);
            }

            prevAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index - 1 >= 0) {
                        goToAtraccion(atraccions, index - 1);
                    } else {
                        onBackPressed();
                    }
                }
            });

        }

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        atraccionTabAdp = new AtraccionTabAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(atraccionTabAdp);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void goToAtraccion(List<Atraccion> atraccions, int pos) {
        Intent atrAct = new Intent(this, AtraccionActivity.class);
        atrAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        atrAct.putExtra(Consts.RECORRIDO, true);
        atrAct.putExtra(Consts._ID, atraccions.get(pos)._id);
        atrAct.putExtra(Consts.POS, pos);
        atrAct.putParcelableArrayListExtra(Consts.ATRACC, (ArrayList<Atraccion>) atraccions);

        startActivity(atrAct);
        finish();
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
