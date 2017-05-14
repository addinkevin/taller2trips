package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.adapters.RecorridosListAdp;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiverOnFavRecorrido extends BroadcastReceiver {

    private final TripTP tripTP;
    private RecorridosListAdp recorridoAdp;
    private Activity activity;

    public ReceiverOnFavRecorrido(Activity activity, RecorridosListAdp recorridoList) {
        this.activity = activity;
        tripTP = (TripTP)activity.getApplication();
        this.recorridoAdp = recorridoList;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONArray rec = new JSONArray(jsonOut);
                    String urlConstImg = tripTP.getUrl() + Consts.ATRACC + "/";

                    for (int i = 0; i < rec.length(); i++) {
                        JSONObject favorito = rec.getJSONObject(i);
                        Recorrido recorrido = new Recorrido(favorito.getJSONObject(Consts.ID_RECORRIDO));
                        recorridoAdp.add(recorrido);

                        recorridoAdp.setIsFav(i, true);
                        recorridoAdp.setNeedUpdateToPos(i, true);
                        recorridoAdp.setId_fav(i, favorito.getString(Consts._ID));

                        Atraccion atraccion = recorrido.getFirstAtraccion();
                        if (!atraccion.fotosPath.isEmpty()) {
                            String firstImg = atraccion.fotosPath.get(0); //primer imagen para mostrar
                            String urlImg = urlConstImg + atraccion._id + Consts.IMAGEN +
                                    "?" + Consts.FILENAME + "=" + firstImg;

                            InternetClient client = new ImageClient(activity.getApplicationContext(),
                                    Consts.GET_REC_FIRST_ATR_IMG, urlImg, null, Consts.GET, null, true, i);
                            client.createAndRunInBackground();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

