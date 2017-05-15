package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PC on 15/05/2017.
 */

public class ReceiverOnVisitAtraccion extends BroadcastReceiver {

    private final Activity act;
    private final AtraccionesListAdp adp;
    private final TripTP tripTP;

    public ReceiverOnVisitAtraccion(Activity activity, AtraccionesListAdp atraccionesAdp) {
        this.act = activity;
        this.adp = atraccionesAdp;
        tripTP = (TripTP) activity.getApplication();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONArray jsonA = new JSONArray(jsonOut);
                    String urlConstImg = tripTP.getUrl() + Consts.ATRACC + "/";

                    for (int i = 0; i < jsonA.length(); i++) {
                        JSONObject visit = jsonA.getJSONObject(i);
                        Atraccion atraccion = new Atraccion(visit.getJSONObject(Consts.ID_ATR));
                        adp.add(atraccion);

                        adp.setIsVisit(i, true);
                        adp.setNeedUpdateToPos(i, true);
                        adp.setId_visit(i, visit.getString(Consts._ID));

                        if (!atraccion.fotosPath.isEmpty()) {
                            String urlImg = urlConstImg + atraccion._id + Consts.IMAGEN + "?" + Consts.FILENAME + "=";
                            String firstImg = atraccion.fotosPath.get(0); //primer imagen para mostrar
                            String url = urlImg + firstImg;

                            InternetClient client = new ImageClient(act.getApplicationContext(),
                                    Consts.GET_ATR_IMG, url, null, Consts.GET, null, true, i);
                            client.createAndRunInBackground();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Error JSON", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(context,"Error json vacio", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,"Error Conexión", Toast.LENGTH_LONG).show();
        }
    }
}
