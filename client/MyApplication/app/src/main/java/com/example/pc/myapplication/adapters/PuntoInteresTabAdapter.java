package com.example.pc.myapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pc.myapplication.AtraccionTab;
import com.example.pc.myapplication.ComentariosPITab;
import com.example.pc.myapplication.ComentariosTab;
import com.example.pc.myapplication.PuntoInteresTab;
import com.example.pc.myapplication.R;


public class PuntoInteresTabAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final Fragment[] pagesLayout;
    private final int commentIndex;
    private final String[] tabTitles;

    public PuntoInteresTabAdapter(FragmentManager fm, Context activity) {
        super(fm);
        this.context = activity;
        String info = context.getResources().getString(R.string.informacion);
        String comments = context.getResources().getString(R.string.comments);

        pagesLayout = new Fragment[2];
        pagesLayout[0] = new PuntoInteresTab();
        pagesLayout[1] = new ComentariosPITab();
        commentIndex = 1;
        tabTitles = new String[] { info,comments };

        ((PuntoInteresTab) pagesLayout[0]).setComentariosTab((ComentariosPITab) pagesLayout[commentIndex]);
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

    public void makeComment() {
        ((ComentariosPITab)pagesLayout[commentIndex]).makeComment();
    }
}
