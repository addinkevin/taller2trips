package com.example.pc.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.pc.myapplication.adapters.DepthPageTransformer;
import com.example.pc.myapplication.adapters.FullScreenImageAdapter;
import com.example.pc.myapplication.singletons.ImagesSingleton;


public class FullScreenViewActivity extends Activity{

    private FullScreenImageAdapter adapter;
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

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this);

        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        // displaying selected image first
        viewPager.setCurrentItem(ImagesSingleton.getInstance().getCurrentPosition());
    }

    @Override
    public void onBackPressed() {
        ImagesSingleton.getInstance().setCurrent(viewPager.getCurrentItem());
        finish();
    }
}