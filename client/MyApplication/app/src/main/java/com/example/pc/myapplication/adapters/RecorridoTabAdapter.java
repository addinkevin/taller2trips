package com.example.pc.myapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pc.myapplication.AtraccionTab;
import com.example.pc.myapplication.ComentariosTab;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.RecorridoMapTab;
import com.example.pc.myapplication.RecorridoTab;
import com.example.pc.myapplication.ciudadesTools.Atraccion;


public class RecorridoTabAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[];
    private Fragment pagesLayout[] = new Fragment[2];
    private Context context;

    public RecorridoTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        pagesLayout[0] = new RecorridoTab();
        pagesLayout[1] = new RecorridoMapTab();

        String info = context.getResources().getString(R.string.informacion);
        String map = context.getResources().getString(R.string.mapa);
        tabTitles = new String[] { info,map };

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

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        ((RecorridoTab)pagesLayout[0]).onRestoreInstanceState(savedInstanceState);
    }

}
