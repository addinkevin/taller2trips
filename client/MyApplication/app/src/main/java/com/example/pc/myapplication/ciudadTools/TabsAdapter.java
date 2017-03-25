package com.example.pc.myapplication.ciudadTools;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pc.myapplication.R;

public class TabsAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[];
    private Fragment pagesLayout[] = new Fragment[3];
    private Context context;

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        pagesLayout[0] = new MainFragment();
        pagesLayout[1] = new AtraccionesFragment();
        pagesLayout[2] = new RecorridosFragment();
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
