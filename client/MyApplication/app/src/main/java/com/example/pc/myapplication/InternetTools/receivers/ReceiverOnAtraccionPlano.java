package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;

public class ReceiverOnAtraccionPlano extends BroadcastReceiver {

    private View view;

    public ReceiverOnAtraccionPlano(View view) {
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        ImageView img = (ImageView) view.findViewById(R.id.planoIMG);
        TextView noPlano = (TextView) view.findViewById(R.id.noPlano);
        if (succes) {
            Bitmap imagePlano = intent.getParcelableExtra(Consts.IMG_OUT);
            if (imagePlano != null) {
                img.setImageBitmap(imagePlano);;
            } else {
                img.setVisibility(View.GONE);
                noPlano.setVisibility(View.VISIBLE);
                //Toast.makeText(context,"Error Bitmap", Toast.LENGTH_LONG).show();
            }
        } else {
            img.setVisibility(View.GONE);
            noPlano.setVisibility(View.VISIBLE);
            //Toast.makeText(context,"Error conexión", Toast.LENGTH_LONG).show();
        }
    }
}
