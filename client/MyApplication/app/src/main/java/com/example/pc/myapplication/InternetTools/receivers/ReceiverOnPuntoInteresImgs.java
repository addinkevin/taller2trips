package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pc.myapplication.carruselTools.CarruselPagerAdapter;
import com.example.pc.myapplication.carruselTools.CarruselPagerPIAdapter;
import com.example.pc.myapplication.commonfunctions.Consts;

/**
 * Created by PC on 19/05/2017.
 */

public class ReceiverOnPuntoInteresImgs extends BroadcastReceiver{
    private final View view;
    private CarruselPagerPIAdapter adapter;
    private ViewPager pager;

    public ReceiverOnPuntoInteresImgs(View view, CarruselPagerPIAdapter adapter, ViewPager pager) {
        this.view = view;
        this.adapter = adapter;
        this.pager = pager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        Log.i("IMGConn", "Termino descarga imagen ");
        if (succes) {
            Bitmap imagePI = intent.getParcelableExtra(Consts.IMG_OUT);
            if (imagePI != null) {
                adapter.addImage(imagePI);

            } else {
                Toast.makeText(context,"Error Bitmap", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,"Error Conexión 2", Toast.LENGTH_LONG).show();
        }
    }
}
