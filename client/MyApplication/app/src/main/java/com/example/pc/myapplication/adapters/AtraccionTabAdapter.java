package com.example.pc.myapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pc.myapplication.AtraccionTab;
import com.example.pc.myapplication.ComentariosTab;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Atraccion;

public class AtraccionTabAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[];
    private Fragment pagesLayout[] = new Fragment[2];
    private Context context;

    public AtraccionTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        pagesLayout[0] = new AtraccionTab();
        pagesLayout[1] = new ComentariosTab();

        ((AtraccionTab) pagesLayout[0]).setComentariosTab((ComentariosTab) pagesLayout[1]);

        String info = context.getResources().getString(R.string.informacion);
        String comments = context.getResources().getString(R.string.comments);
        tabTitles = new String[] { info,comments };

    }

    public void attachAtraccion(Atraccion atraccion) {
        ((ComentariosTab) pagesLayout[1]).attachAtraccion(atraccion);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
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
        ((AtraccionTab)pagesLayout[0]).onBackPressed();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        ((AtraccionTab)pagesLayout[0]).onRestoreInstanceState(savedInstanceState);
    }

    public void showAudioController() {
        ((AtraccionTab)pagesLayout[0]).showAudioController();
    }

    public void audioClick() {
        ((AtraccionTab)pagesLayout[0]).audioClick();
    }

    public void makeComment() {
        ((ComentariosTab)pagesLayout[1]).makeComment();
    }

}
