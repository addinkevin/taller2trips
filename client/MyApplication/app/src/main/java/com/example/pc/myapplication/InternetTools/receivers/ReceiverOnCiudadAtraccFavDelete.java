package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.List;

public class ReceiverOnCiudadAtraccFavDelete extends BroadcastReceiver{
    private final AtraccionesListAdp atraccionesAdp;

    public ReceiverOnCiudadAtraccFavDelete(AtraccionesListAdp atraccionesAdp) {
        this.atraccionesAdp = atraccionesAdp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        int id = intent.getIntExtra(Consts.URL_ID, -1);
        atraccionesAdp.setIsFav(id, !succes);//si succes entonces se elimino el favorito y ya no es mas favorito
        atraccionesAdp.setNeedUpdateToPos(id, true);
        atraccionesAdp.notifyDataSetChanged();
    }
}
