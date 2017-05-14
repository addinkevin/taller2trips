package com.example.pc.myapplication.dialogs;

import android.app.*;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Publicidad;

public class PublicidadDialog {

    public static void show(Activity act, Publicidad publicidad) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(act);
        LayoutInflater infl = act.getLayoutInflater();
        View view = infl.inflate(R.layout.publicidad, null);

        TextView nombre = (TextView) view.findViewById(R.id.nombrePub);
        TextView link = (TextView) view.findViewById(R.id.link);
        TextView infoText = (TextView) view.findViewById(R.id.infoText);
        ImageView image = (ImageView) view.findViewById(R.id.imagePub);

        image.setImageBitmap(publicidad.getImagen());
        nombre.setText(publicidad.getNombre());
        link.setText(publicidad.getLink());
        infoText.setText(publicidad.getDescripcion());
        builder.setTitle(publicidad.getNombre());
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

        }).setPositiveButton(R.string.share_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
