package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadTools.AtraccionesFragment;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by PC on 26/03/2017.
 */

public class ReceiverOnCiudadAtracc extends BroadcastReceiver {
    private final AtraccionesFragment atraccionesFragment;
    private final View myFragmentView;
    private List<Atraccion> atraccionItems;

    public ReceiverOnCiudadAtracc(AtraccionesFragment atraccionesFragment, View myFragmentView, List<Atraccion> atraccionItems) {
        this.atraccionesFragment = atraccionesFragment;
        this.myFragmentView = myFragmentView;
        this.atraccionItems = atraccionItems;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONArray jsonA = new JSONArray(jsonOut);
                    for (int i = 0; i < jsonA.length(); i++) {
                        Atraccion atraccion = new Atraccion(jsonA.getJSONObject(i));
                        atraccionItems.add(atraccion);

                        if (!atraccion.fotosPath.isEmpty()) {
                            String firstImg = atraccion.fotosPath.get(0); //primer imagen para mostrar
                            String url = ((TripTP)atraccionesFragment.getActivity().getApplication()).getUrl() + Consts.ATRACC + "/" + atraccion._id +
                                    Consts.IMAGEN + "?" + Consts.FILENAME + "=" + firstImg;

                            Log.i("IMGConnect", "Comienzo la descarga Imagen " + i);

                            InternetClient client = new ImageClient(atraccionesFragment.getContext(), myFragmentView,
                                    Consts.GET_ATR_IMG, url, null, Consts.GET, null, true, i);
                            client.runInBackground();
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