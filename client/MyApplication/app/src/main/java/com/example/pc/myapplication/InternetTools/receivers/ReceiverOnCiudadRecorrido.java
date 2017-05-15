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

import java.util.Map;

public class ReceiverOnCiudadRecorrido extends BroadcastReceiver {

    private final TripTP tripTP;
    private RecorridosListAdp recorridoAdp;
    private Activity activity;

    public ReceiverOnCiudadRecorrido(Activity activity, RecorridosListAdp recorridoList) {
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
                    String urlConstFav = tripTP.getUrl() + Consts.FAVS + Consts.BUSCAR
                            + "?" + Consts.ID_USER + "=" + tripTP.getUserID_fromServ()
                            + "&" + Consts.ID_RECORRIDO + "=" ;

                    for (int i = 0; i < rec.length(); i++) {
                        Recorrido recorrido = new Recorrido(rec.getJSONObject(i));
                        recorridoAdp.add(recorrido);
                        Atraccion atraccion = recorrido.getFirstAtraccion();
                        String firstImg = atraccion.fotosPath.get(0); //primer imagen para mostrar
                        String urlImg = urlConstImg + atraccion._id + Consts.IMAGEN +
                                "?" + Consts.FILENAME + "=" + firstImg;

                        InternetClient client = new ImageClient(activity.getApplicationContext(),
                                Consts.GET_REC_FIRST_ATR_IMG, urlImg, null, Consts.GET, null, true, i);
                        client.createAndRunInBackground();

                        if (tripTP.isLogin()) {
                            String urlAtrFav = urlConstFav + recorrido.get_id();
                            Map<String,String> headers = Consts.getHeaderPaginadoTipoBusqueda("0",Consts.TIPO_BUSQ_TODOS);
                            InternetClient clientFav = new InfoClient(activity.getApplicationContext(),
                                    Consts.GEToPOST_REC_FAV, urlAtrFav, headers, Consts.GET, null, true, i);
                            clientFav.createAndRunInBackground();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
