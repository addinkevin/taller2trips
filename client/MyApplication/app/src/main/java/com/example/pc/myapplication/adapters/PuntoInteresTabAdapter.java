package com.example.pc.myapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pc.myapplication.PuntoInteresTab;
import com.example.pc.myapplication.R;


public class PuntoInteresTabAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final Fragment[] pagesLayout;
    private final String[] tabTitles;

    public PuntoInteresTabAdapter(FragmentManager fm, Context activity) {
        super(fm);
        this.context = activity;
        String info = context.getResources().getString(R.string.informacion);

        pagesLayout = new Fragment[1];
        pagesLayout[0] = new PuntoInteresTab();
        tabTitles = new String[] { info };

    }

    @Override
    public int getCount() {
        return pagesLayout.length;
    }

    @Override
    public Fragment getItem(int position) {
        return pagesLayout[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


    public void onBackPressed() {
        ((PuntoInteresTab)pagesLayout[0]).onBackPressed();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        ((PuntoInteresTab)pagesLayout[0]).onRestoreInstanceState(savedInstanceState);
    }

    public void showAudioController() {
        ((PuntoInteresTab)pagesLayout[0]).showAudioController();
    }

    public void audioClick() {
        ((PuntoInteresTab)pagesLayout[0]).audioClick();
    }
}
