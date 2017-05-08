package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.adapters.RecorridosListAdp;
import com.example.pc.myapplication.commonfunctions.Consts;


public class ReceiverOnCiudadRecFavDelete extends BroadcastReceiver{
    private final RecorridosListAdp recorridosListAdp;

    public ReceiverOnCiudadRecFavDelete(RecorridosListAdp recorridosAdp) {
        this.recorridosListAdp = recorridosAdp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        int id = intent.getIntExtra(Consts.URL_ID, -1);
        recorridosListAdp.setIsFav(id, !succes);//si succes entonces se elimino el favorito y ya no es mas favorito
        recorridosListAdp.setNeedUpdateToPos(id, true);
        recorridosListAdp.notifyDataSetChanged();
    }
}
