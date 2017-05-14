package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;

public class ReceiverOnCiudadAtracc extends BroadcastReceiver {
    private final Activity act;
    private final TripTP tripTP;
    private AtraccionesListAdp adp;

    public ReceiverOnCiudadAtracc(Activity atraccionesFragment, AtraccionesListAdp adp) {
        this.act = atraccionesFragment;
        this.adp = adp;
        tripTP = (TripTP) act.getApplication();
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
                    String urlConstFav = tripTP.getUrl() + Consts.FAVS + Consts.BUSCAR
                            + "?" + Consts.ID_USER + "=" + tripTP.getUserID_fromServ()
                            + "&" + Consts.ID_ATR + "=" ;
                    for (int i = 0; i < jsonA.length(); i++) {
                        Atraccion atraccion = new Atraccion(jsonA.getJSONObject(i));
                        adp.add(atraccion);

                        if (tripTP.isLogin()) {
                            String urlAtrFav = urlConstFav + atraccion._id;
                            InternetClient clientFav = new InfoClient(act.getApplicationContext(),
                                    Consts.GEToPOST_ATR_FAV, urlAtrFav, null, Consts.GET, null, true, i);
                            clientFav.createAndRunInBackground();
                        }

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
