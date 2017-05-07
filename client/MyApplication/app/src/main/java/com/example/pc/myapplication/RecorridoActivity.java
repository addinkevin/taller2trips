package com.example.pc.myapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pc.myapplication.adapters.AtraccionTabAdapter;
import com.example.pc.myapplication.adapters.RecorridoTabAdapter;

public class RecorridoActivity extends AppCompatActivity {

    private RecorridoTabAdapter atraccionTabAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        atraccionTabAdp = new RecorridoTabAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(atraccionTabAdp);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }
}
