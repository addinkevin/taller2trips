package com.example.pc.myapplication;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pc.myapplication.adapters.RecorridoTabAdapter;

public class RecorridoActivity extends AppCompatActivity {

    private RecorridoTabAdapter recorridoTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        recorridoTabAdapter = new RecorridoTabAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(recorridoTabAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recorridoTabAdapter.onRestoreInstanceState(savedInstanceState);
    }
}
