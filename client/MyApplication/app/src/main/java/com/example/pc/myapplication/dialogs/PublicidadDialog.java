package com.example.pc.myapplication.dialogs;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Publicidad;

public class PublicidadDialog {

    public static void show(final Activity act, Publicidad publicidad) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(act);
        LayoutInflater infl = act.getLayoutInflater();
        View view = infl.inflate(R.layout.publicidad, null);

        TextView nombre = (TextView) view.findViewById(R.id.nombrePub);
        TextView link = (TextView) view.findViewById(R.id.link);
        TextView infoText = (TextView) view.findViewById(R.id.infoText);
        ImageView image = (ImageView) view.findViewById(R.id.imagePub);

        final String linkS = publicidad.getLink();

        final String url;
        if (!linkS.startsWith("http://") && !linkS.startsWith("https://")) {
            url = "http://" + linkS;
        } else {
            url = linkS;
        }

        image.setImageBitmap(publicidad.getImagen());
        nombre.setText(publicidad.getNombre());
        link.setText(Html.fromHtml( "<a href=\"" + url + "\">" + url + "</a> "));
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                act.startActivity(browserIntent);
            }
        });
        infoText.setText(publicidad.getDescripcion());
        builder.setTitle(R.string.app_name);
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

        }).setPositiveButton(R.string.accept_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                act.startActivity(browserIntent);
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
