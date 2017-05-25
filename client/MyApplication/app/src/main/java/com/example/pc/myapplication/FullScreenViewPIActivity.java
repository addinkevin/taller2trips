package com.example.pc.myapplication;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pc.myapplication.adapters.DepthPageTransformer;
import com.example.pc.myapplication.adapters.FullScreenImagePIAdapter;
import com.example.pc.myapplication.singletons.ImagesSingletonPI;

public class FullScreenViewPIActivity extends AppCompatActivity {

    private FullScreenImagePIAdapter adapter;
    private ViewPager viewPager;


    private void hideClockBateryBar(){
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        hideClockBateryBar();

        //Set the page and the animator depthPageTransformer
        viewPager = (ViewPager) findViewById(R.id.pager);
        //viewPager.setPageTransformer(true,new DepthPageTransformer());

        adapter = new FullScreenImagePIAdapter(FullScreenViewPIActivity.this);

        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        // displaying selected image first
        viewPager.setCurrentItem(ImagesSingletonPI.getInstance().getCurrentPosition());
    }

    @Override
    public void onBackPressed() {
        ImagesSingletonPI.getInstance().setCurrent(viewPager.getCurrentItem());
        finish();
    }
}
