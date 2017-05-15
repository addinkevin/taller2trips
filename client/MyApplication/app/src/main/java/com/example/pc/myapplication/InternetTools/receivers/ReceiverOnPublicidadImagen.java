package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.pc.myapplication.ciudadesTools.Publicidad;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.PublicidadDialog;

public class ReceiverOnPublicidadImagen extends BroadcastReceiver {

    private Publicidad publicidad;
    private Activity activity;

    public ReceiverOnPublicidadImagen(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            Bitmap imageAtracc = intent.getParcelableExtra(Consts.IMG_OUT);
            if (imageAtracc != null && publicidad != null) {
                publicidad.setImagen(imageAtracc);
                PublicidadDialog.show(activity, publicidad);
            }
        }
    }

    public void setPublicidad(Publicidad publicidad) {
        this.publicidad = publicidad;
    }
}
