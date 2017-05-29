package com.example.pc.myapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pc.myapplication.AtraccionTab;
import com.example.pc.myapplication.ComentariosTab;
import com.example.pc.myapplication.PuntoInteresesTab;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Atraccion;

public class AtraccionTabAdapter extends FragmentPagerAdapter {

    private String tabTitles[];
    private Fragment pagesLayout[];
    private Context context;
    private int commentIndex;

    public AtraccionTabAdapter(FragmentManager fm, boolean recorrible, Context context) {
        super(fm);
        this.context = context;

        String info = context.getResources().getString(R.string.informacion);
        String comments = context.getResources().getString(R.string.comments);
        String interest = context.getResources().getString(R.string.interest);

        if (recorrible) {
            pagesLayout = new Fragment[3];
            pagesLayout[0] = new AtraccionTab();
            pagesLayout[1] = new PuntoInteresesTab();
            pagesLayout[2] = new ComentariosTab();
            commentIndex = 2;
            tabTitles = new String[] { info,interest,comments };
        } else {
            pagesLayout = new Fragment[2];
            pagesLayout[0] = new AtraccionTab();
            pagesLayout[1] = new ComentariosTab();
            commentIndex = 1;
            tabTitles = new String[] { info,comments };
        }

        ((AtraccionTab) pagesLayout[0]).setComentariosTab((ComentariosTab) pagesLayout[commentIndex]);
        if(recorrible)
            ((AtraccionTab) pagesLayout[0]).setPuntoInteresesTab((PuntoInteresesTab) pagesLayout[1]);

    }

    public void attachAtraccion(Atraccion atraccion) {
        ((ComentariosTab) pagesLayout[commentIndex]).attachAtraccion(atraccion);
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
        ((ComentariosTab)pagesLayout[commentIndex]).makeComment();
    }

}
