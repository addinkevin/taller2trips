package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by PC on 09/04/2017.
 */

public class ReceiverOnAtrNearInMap extends BroadcastReceiver {


    private List<Atraccion> atraccionItems;
    private GoogleMap map;

    public ReceiverOnAtrNearInMap(List<Atraccion> atraccionItems) {
        this.atraccionItems = atraccionItems;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes && map != null) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONArray jsonA = new JSONArray(jsonOut);
                    for (int i = 0; i < jsonA.length(); i++) {
                        Atraccion atraccion = new Atraccion(jsonA.getJSONObject(i));
                        atraccionItems.add(atraccion);
                        LatLng pos = new LatLng(atraccion.latitud,atraccion.longitud);
                        map.addMarker(new MarkerOptions()
                                .title(atraccion.nombre)
                                .snippet(atraccion.descripcion)
                                .position(pos)
                                .flat(true))
                                .setTag(i);

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

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
