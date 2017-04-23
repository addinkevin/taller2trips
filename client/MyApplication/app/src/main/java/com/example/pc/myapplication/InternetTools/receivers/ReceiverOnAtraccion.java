package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pc.myapplication.AtraccionTab;
import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by PC on 26/03/2017.
 */

public class ReceiverOnAtraccion extends BroadcastReceiver {

    private AtraccionTab atrAct;
    private View view;

    public ReceiverOnAtraccion(AtraccionTab atraccionActivity, View view) {
        this.atrAct = atraccionActivity;
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    Atraccion atraccion = new Atraccion(new JSONObject(jsonOut));
                    atrAct.attachAtraccion(atraccion);
                    List fotos = atraccion.fotosPath;

                    TripTP app = ((TripTP) atrAct.getActivity().getApplication());
                    String urlConst = app.getUrl() + Consts.ATRACC + "/" + atraccion._id
                            + Consts.IMAGEN + "?" + Consts.FILENAME + "=";

                    for(int i = 0; i < fotos.size(); i++) {
                        String urlIMG = urlConst  + fotos.get(i);

                        InternetClient client = new ImageClient(atrAct.getActivity().getApplicationContext(),
                                Consts.GET_ATR_IMG_S, urlIMG, null, Consts.GET, null, true, -1);
                        client.runInBackground();
                    }

                    String urlPlano = app.getUrl() + Consts.ATRACC + "/" + atraccion._id + Consts.PLANO;
                    Log.i("IMGConn", "Comienza descarga imagen Plano");
                    InternetClient client = new ImageClient(atrAct.getActivity().getApplicationContext(),
                            Consts.GET_ATR_PLANO, urlPlano, null, Consts.GET, null, true, -1);
                    client.runInBackground();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Error JSON", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.i("IMGConn", "Error JSon vacio");
                Toast.makeText(context,"Error JSon vacio", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.i("IMGConn", "Error Conexión");
            Toast.makeText(context,"Error Conexión", Toast.LENGTH_LONG).show();
        }
    }
}
