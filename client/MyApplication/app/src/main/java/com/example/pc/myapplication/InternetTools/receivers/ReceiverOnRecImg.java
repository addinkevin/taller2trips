package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.adapters.RecorridosListAdp;
import com.example.pc.myapplication.commonfunctions.Consts;

public class ReceiverOnRecImg extends BroadcastReceiver {

    private ImageView fragView;

    public ReceiverOnRecImg(View fragView) {
        this.fragView = (ImageView)fragView.findViewById(R.id.imgRec);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            Bitmap imageAtracc = intent.getParcelableExtra(Consts.IMG_OUT);
            if (imageAtracc != null ) {
                fragView.setImageBitmap(imageAtracc);
            } else {
                Toast.makeText(context,"Error Bitmap", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,"Falló la conexión", Toast.LENGTH_LONG).show();
        }
    }
}
