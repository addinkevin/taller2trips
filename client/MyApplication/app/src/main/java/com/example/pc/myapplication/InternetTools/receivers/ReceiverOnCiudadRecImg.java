package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.example.pc.myapplication.adapters.RecorridosListAdp;
import com.example.pc.myapplication.commonfunctions.Consts;

public class ReceiverOnCiudadRecImg extends BroadcastReceiver {

    private RecorridosListAdp recorridosAdp;

    public ReceiverOnCiudadRecImg(RecorridosListAdp recorridosAdp) {
        this.recorridosAdp = recorridosAdp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            Bitmap imageAtracc = intent.getParcelableExtra(Consts.IMG_OUT);
            int imgID = intent.getIntExtra(Consts.IMG_ID, -1);
            if (imageAtracc != null && imgID >= 0) {
                recorridosAdp.addImgToPos(imageAtracc, imgID);
            } else {
                Toast.makeText(context,"Error Bitmap", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,"Falló la conexión", Toast.LENGTH_LONG).show();
        }
    }
}
