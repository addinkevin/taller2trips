package com.example.pc.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.myapplication.adapters.RecorridoAtraccionesAdp;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;

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

        RecorridoAtraccionesAdp recorridoListAdp = new RecorridoAtraccionesAdp(getActivity(),recorrido.getAtracciones());

        ListView recList = (ListView) fragView.findViewById(R.id.recList);
        recList.setAdapter(recorridoListAdp);
        recList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                empezarEn(position);
            }
        });

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
