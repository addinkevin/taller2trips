package com.example.pc.myapplication;



import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;


import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadImage;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudades;
import com.example.pc.myapplication.ciudadTools.TabsAdapter;
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.commonfunctions.Consts;

public class CiudadActivity extends AppCompatActivity {

    ViewPager mViewPager;
    private ReceiverOnCiudadImage onCiudadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_ciudad);

        View view = findViewById(R.id.ciudadL);

        Ciudad ciudad = new Ciudad (getIntent().getStringExtra(Consts.NOMBRE),
                getIntent().getStringExtra(Consts.DESCRIPCION),
                getIntent().getStringExtra(Consts.PAIS),
                getIntent().getStringExtra(Consts._ID));



        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle("Taller2Trips");
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(),this, ciudad));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }
}