package com.example.pc.myapplication.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pc.myapplication.CiudadAtraccionesTab;
import com.example.pc.myapplication.CiudadTab;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.CiudadRecorridosTab;
import com.example.pc.myapplication.ciudadesTools.Ciudad;

public class TabsAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
        private String tabTitles[];
        private Fragment pagesLayout[] = new Fragment[3];
        private Context context;

    public TabsAdapter(FragmentManager fm, Context context, Ciudad ciudad) {
            super(fm);
            this.context = context;

            pagesLayout[0] = new CiudadTab();
            ((CiudadTab)pagesLayout[0]).setCiudad(ciudad);
            pagesLayout[1] = new CiudadAtraccionesTab();
            ((CiudadAtraccionesTab)pagesLayout[1]).setCiudad(ciudad);
            pagesLayout[2] = new CiudadRecorridosTab();
            ((CiudadRecorridosTab)pagesLayout[2]).setCiudad(ciudad);

            String info = context.getResources().getString(R.string.informacion);
            String tours = context.getResources().getString(R.string.recorridos);
            String atracciones = context.getResources().getString(R.string.atracciones);
            tabTitles = new String[] { info,atracciones,tours };

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



}
