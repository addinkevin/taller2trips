package com.example.pc.myapplication.carruselTools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.R;

public class CarruselPagerAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private CarruselLinearLayout cur = null;
    private CarruselLinearLayout next = null;
    private AtraccionActivity context;
    private FragmentManager fm;
    private float scale;

    public CarruselPagerAdapter(AtraccionActivity context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        if (position == AtraccionActivity.FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;

        position = position % AtraccionActivity.PAGES;
        return CarruselFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount() {
        return AtraccionActivity.PAGES * AtraccionActivity.LOOPS;
    }

    @Override
    public void transformPage(View page, float position) {
        CarruselLinearLayout myLinearLayout = (CarruselLinearLayout) page.findViewById(R.id.root);
        float scale = BIG_SCALE;
        if (position > 0) {
            scale = scale - position * DIFF_SCALE;
        } else {
            scale = scale + position * DIFF_SCALE;
        }
        if (scale < 0) scale = 0;
        myLinearLayout.setScaleBoth(scale);
    }
}
