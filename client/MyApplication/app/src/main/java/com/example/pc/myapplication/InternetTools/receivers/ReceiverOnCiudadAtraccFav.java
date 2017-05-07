package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.JsonReader;

import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

public class ReceiverOnCiudadAtraccFav extends BroadcastReceiver {

    private final AtraccionesListAdp atraccionesAdp;

    public ReceiverOnCiudadAtraccFav(AtraccionesListAdp atraccionesAdp) {
        this.atraccionesAdp = atraccionesAdp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
        int id = intent.getIntExtra(Consts.URL_ID, -1);
        atraccionesAdp.setIsFav(id, succes && jsonOut != null);
        atraccionesAdp.setNeedUpdateToPos(id, true);
        if ( ((Atraccion)atraccionesAdp.getItem(id)).isFav()) {
            try {
                JSONObject atrFav = new JSONObject(jsonOut);
                atraccionesAdp.setId_fav(id, atrFav.getString(Consts._ID));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        atraccionesAdp.notifyDataSetChanged();

    }
}
