package com.example.pc.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.myapplication.adapters.RecorridoAtraccionesAdp;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class RecorridoTab  extends Fragment {

    private View fragView;
    private Recorrido recorrido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.recorrido_tab, container, false);

        if ( recorrido == null ) {
            recorrido = getActivity().getIntent().getParcelableExtra(Consts.ID_RECORRIDO);
        }

        TextView nombreRec = (TextView) fragView.findViewById(R.id.nombreRec);
        nombreRec.setText(recorrido.getNombre());

        TextView descripRec = (TextView) fragView.findViewById(R.id.infoText);
        descripRec.setText(recorrido.getDescripcion());

        ImageView imgRec = (ImageView) fragView.findViewById(R.id.imgRec);
        imgRec.setImageBitmap(recorrido.getFotoRecorrido());
        LinearLayout linRec = (LinearLayout) fragView.findViewById(R.id.recorridosLin);

        TextView text = (TextView) fragView.findViewById(R.id.nombreAtr);
        ViewGroup.LayoutParams params = text.getLayoutParams();
        linRec.removeView(text);
        ViewGroup.LayoutParams dividerParams = linRec.getChildAt(0).getLayoutParams();

        List<Atraccion> atracciones = recorrido.getAtracciones();

        for (int i = 0; i < atracciones.size(); i++) {
            TextView nombre = new TextView(getContext());
            nombre.setLayoutParams(params);
            nombre.setText(atracciones.get(i).nombre);
            nombre.setGravity(Gravity.CENTER);

            final int finalI = i;
            nombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    empezarEn(finalI);
                }
            });
            linRec.addView(nombre);
            View divider = new View(getContext());
            divider.setBackgroundColor(Color.parseColor("#706f6f"));
            divider.setLayoutParams(dividerParams);
            linRec.addView(divider);
        }

        Button comenzar = (Button) fragView.findViewById(R.id.comenzar);
        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empezarEn(0);
            }
        });

        return fragView;
    }

    public void empezarEn(int index) {
        Intent atrAct = new Intent(getActivity(), AtraccionActivity.class);
        atrAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        atrAct.putExtra(Consts.RECORRIDO_POPULATE, true);
        atrAct.putExtra(Consts._ID, recorrido.getAtraccionAt(index)._id);
        atrAct.putExtra(Consts.ATR_RECORRIBLE, recorrido.getAtraccionAt(index).isRecorrible());
        atrAct.putExtra(Consts.POS, index);
        atrAct.putParcelableArrayListExtra(Consts.ATRACC, (ArrayList<Atraccion>) recorrido.getAtracciones());
        startActivity(atrAct);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if ( recorrido != null)
            savedInstanceState.putParcelable(Consts.ID_RECORRIDO, recorrido);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        recorrido = savedInstanceState.getParcelable(Consts.ID_RECORRIDO);
    }
}
