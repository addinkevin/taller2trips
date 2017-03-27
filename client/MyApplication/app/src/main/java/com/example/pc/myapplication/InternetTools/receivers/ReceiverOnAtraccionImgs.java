package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.carruselTools.CarruselPagerAdapter;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

public class ReceiverOnAtraccionImgs extends BroadcastReceiver {
    private final AtraccionActivity atraccionActivity;
    private final View view;
    private CarruselPagerAdapter adapter;

    public ReceiverOnAtraccionImgs(AtraccionActivity atraccionActivity, View view, CarruselPagerAdapter adapter) {
        this.atraccionActivity = atraccionActivity;
        this.view = view;
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            Bitmap imageAtracc = intent.getParcelableExtra(Consts.IMG_OUT);
            if (imageAtracc != null) {
                adapter.addImage(imageAtracc);
                //adapter.notifyDataSetChanged();

                Atraccion atraccion = atraccionActivity.getAtraccion();
                if (atraccion != null && atraccion.fotosPath.size() == adapter.getSize() ) {
                    ViewPager pager = (ViewPager) atraccionActivity.findViewById(R.id.myviewpager);

                    pager.setAdapter(adapter);
                    pager.setPageTransformer(false, adapter);

                    // Set current item to the middle page so we can fling to both
                    // directions left and right
                    pager.setCurrentItem(atraccionActivity.FIRST_PAGE);

                    // Necessary or the pager will only have one extra page to show
                    // make this at least however many pages you can see
                    pager.setOffscreenPageLimit(3);

                    // Set margin for pages as a negative number, so a part of next and
                    // previous pages will be showed
                    pager.setPageMargin(-200);
                }
            }
        }
    }
}
