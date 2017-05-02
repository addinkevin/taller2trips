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

import com.example.pc.myapplication.adapters.AtraccionTabAdapter;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;

import de.hdodenhof.circleimageview.CircleImageView;

public class AtraccionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AtraccionTabAdapter atraccionTabAdp;
    private TripTP tripTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.drawer_atracciones);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle((R.string.atraccion));
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        tripTP = (TripTP) getApplication();

        View header = navigationView.getHeaderView(0);

        if (tripTP.getImageUser() != null) {
            CircleImageView img = (CircleImageView) header.findViewById(R.id.profile_image);
            img.setImageBitmap(tripTP.getImageUser());
        }

        if (tripTP.getImgBanner() != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                BitmapDrawable ob = new BitmapDrawable(getResources(), tripTP.getImgBanner());
                header.setBackground(ob);
            } else {
                BitmapDrawable ob = new BitmapDrawable(tripTP.getImgBanner());
                header.setBackgroundDrawable(ob);
            }
        }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
