package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.adapters.RecorridosListAdp;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
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
        try {
            JSONArray atrFav = new JSONArray(jsonOut);
            if (atrFav.length() > 0) {
                setIsFavAndIdFav(id,succes && jsonOut != null,atrFav.getJSONObject(0));
            } else {
                setIsFav(id,false);
            }
        } catch (JSONException e) {
            try {
                JSONObject atrFav = new JSONObject(jsonOut);
                setIsFavAndIdFav(id,succes && jsonOut != null,atrFav);
            } catch (JSONException e1) {
                setIsFav(id,false);
            }
        }
        recorridosAdp.notifyDataSetChanged();

    }

    private void setIsFav(int id, boolean isFav){
        recorridosAdp.setIsFav(id, isFav);
        recorridosAdp.setNeedUpdateToPos(id, true);
    }

    private void setIsFavAndIdFav(int id, boolean isFav, JSONObject fav) throws JSONException {
        setIsFav(id,isFav);
        if ( isFav && fav != null) {
            recorridosAdp.setId_fav(id, fav.getString(Consts._ID));
            //Toast.makeText(activity, R.string.addToFavs, Toast.LENGTH_SHORT).show();
        }
    }
}
