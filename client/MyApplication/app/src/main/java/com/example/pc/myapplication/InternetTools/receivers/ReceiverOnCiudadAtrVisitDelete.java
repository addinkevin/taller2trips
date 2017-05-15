package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.commonfunctions.Consts;

public class ReceiverOnCiudadAtrVisitDelete extends BroadcastReceiver {

    private final AtraccionesListAdp atraccionesAdp;

    public ReceiverOnCiudadAtrVisitDelete(AtraccionesListAdp atraccionesAdp) {
        this.atraccionesAdp = atraccionesAdp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        int id = intent.getIntExtra(Consts.URL_ID, -1);
        atraccionesAdp.setIsVisit(id, !succes);//si succes entonces se elimino el favorito y ya no es mas visitado
        atraccionesAdp.setNeedUpdateToPos(id, true);
        atraccionesAdp.notifyDataSetChanged();
    }
}
