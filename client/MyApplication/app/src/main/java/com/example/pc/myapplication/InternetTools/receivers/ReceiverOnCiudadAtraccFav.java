package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiverOnCiudadAtraccFav extends BroadcastReceiver {

    private final AtraccionesListAdp atraccionesAdp;
    private Activity activity;

    // vienen nuevos agregados por el usuario y los que tomo en el get para ver si son favoritos
    public ReceiverOnCiudadAtraccFav(AtraccionesListAdp atraccionesAdp, Activity activity) {
        this.atraccionesAdp = atraccionesAdp;
        this.activity = activity;
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
                setIsFavAndIdFav(id,succes && jsonOut != null, atrFav);
                Toast.makeText(activity, R.string.addToFavs, Toast.LENGTH_SHORT).show();
            } catch (JSONException e1) {
                setIsFav(id,false);
            }
        }
        atraccionesAdp.notifyDataSetChanged();

    }

    private void setIsFav(int id, boolean isFav){
        atraccionesAdp.setIsFav(id, isFav);
        atraccionesAdp.setNeedUpdateToPos(id, true);
    }

    private void setIsFavAndIdFav(int id, boolean isFav, JSONObject fav) throws JSONException {
        setIsFav(id,isFav);
        if ( isFav && fav != null) {
            atraccionesAdp.setId_fav(id, fav.getString(Consts._ID));
        }
    }
}
