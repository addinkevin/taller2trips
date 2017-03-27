package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadTools.MainFragment;
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.commonfunctions.Consts;

public class ReceiverOnCiudadImage extends BroadcastReceiver {

    private View view;
    private Ciudad ciudad;

    public ReceiverOnCiudadImage(Ciudad ciudad, View view) {
        this.ciudad = ciudad;
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            Bitmap imageCiudad = intent.getParcelableExtra(Consts.IMG_OUT);
            if (imageCiudad != null) {
                ImageView img = (ImageView) view.findViewById(R.id.imageTitle);
                img.setImageBitmap(imageCiudad);
                ciudad.imagen = imageCiudad;
            }
        }
    }
}
