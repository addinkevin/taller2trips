package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.adapters.RecorridosListAdp;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiverOnCiudadRecFav extends BroadcastReceiver {

    private final RecorridosListAdp recorridosAdp;

    public ReceiverOnCiudadRecFav(RecorridosListAdp recorridosdp) {
        this.recorridosAdp = recorridosdp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
        int id = intent.getIntExtra(Consts.URL_ID, -1);
        recorridosAdp.setIsFav(id, succes && jsonOut != null);
        recorridosAdp.setNeedUpdateToPos(id, true);
        if ( ((Recorrido) recorridosAdp.getItem(id)).isFav()) {
            try {
                JSONObject atrFav = new JSONObject(jsonOut);
                recorridosAdp.setId_fav(id, atrFav.getString(Consts._ID));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recorridosAdp.notifyDataSetChanged();

    }
}
