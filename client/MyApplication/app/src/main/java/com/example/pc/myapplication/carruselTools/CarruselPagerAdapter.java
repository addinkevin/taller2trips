package com.example.pc.myapplication.carruselTools;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CarruselPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.PageTransformer {
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.5f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private CarruselLinearLayout cur = null;
    private CarruselLinearLayout next = null;
    private AtraccionActivity context;
    private FragmentManager fm;
    private List<Bitmap> imagenes = new ArrayList<>();
    private float scale;

    public CarruselPagerAdapter(AtraccionActivity context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    public int getSize() {
        return imagenes.size();
    }


    public void addImage(Bitmap img) {
        imagenes.add(img);
        Log.i("IMGConn", "agrega imagen " + imagenes.size());
        List<Bitmap> aux = new ArrayList<>(imagenes);
        imagenes.clear();
        imagenes.addAll(aux);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        if (position == context.FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;

        position = position % context.PAGES;
        Bitmap imagen = null;
        Log.i("IMGConn", "Dibuja get " + position);
        if (imagenes.size() > position) {
            Log.i("IMGConn", "Tiene imagen " + position);
            imagen = imagenes.get(position);
        }

        return new CarruselFragment().newInstance(context, position, scale, imagen);
    }

    @Override
    public int getCount() {
        return context.PAGES * AtraccionActivity.LOOPS;
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
