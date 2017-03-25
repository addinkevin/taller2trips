package com.example.pc.myapplication;



import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;


import com.example.pc.myapplication.ciudadTools.TabsAdapter;

public class CiudadActivity extends AppCompatActivity {

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_ciudad);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle("Taller2Trips");
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(),this));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }
}
