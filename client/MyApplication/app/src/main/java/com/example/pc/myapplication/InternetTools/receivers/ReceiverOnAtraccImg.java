package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.pc.myapplication.atraccionesTools.AtraccionesListAdp;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.List;

public class ReceiverOnAtraccImg extends BroadcastReceiver{
    private List<Atraccion> atraccionItems;
    private AtraccionesListAdp atraccionesAdp;

    public ReceiverOnAtraccImg(List<Atraccion> atraccionItems, AtraccionesListAdp atraccionesAdp) {
        this.atraccionItems = atraccionItems;
        this.atraccionesAdp = atraccionesAdp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            Bitmap imageAtracc = intent.getParcelableExtra(Consts.IMG_OUT);
            int imgID = intent.getIntExtra(Consts.IMG_ID, -1);
            if (imageAtracc != null && imgID >= 0) {
                atraccionesAdp.addImg(imageAtracc, imgID);
                atraccionesAdp.notifyDataSetChanged();
            } else {
                Toast.makeText(context,"Error Bitmap", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,"Falló la conexión", Toast.LENGTH_LONG).show();
        }
    }
}