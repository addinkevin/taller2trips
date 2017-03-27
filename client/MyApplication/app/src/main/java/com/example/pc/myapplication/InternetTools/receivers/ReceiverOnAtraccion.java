package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.application.MyApplication;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by PC on 26/03/2017.
 */

public class ReceiverOnAtraccion extends BroadcastReceiver {

    private AtraccionActivity atraccionActivity;
    private View view;

    public ReceiverOnAtraccion(AtraccionActivity atraccionActivity, View view) {
        this.atraccionActivity = atraccionActivity;
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
                    atraccionActivity.attachAtraccion(atraccion);
                    List fotos = atraccion.fotosPath;
                    for(int i = 0; i < fotos.size(); i++) {
                        String url = ((MyApplication)atraccionActivity.getApplication()).getUrl() + Consts.ATRACC + "/" + atraccion._id
                                + Consts.IMAGEN + "?" + Consts.FILENAME + "=" + fotos.get(i);

                        InternetClient client = new ImageClient(atraccionActivity, view,
                                Consts.GET_ATR_IMG_S, url, null, Consts.GET, null, true, -1);
                        client.runInBackground();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Error JSON", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(context,"Error JSon vacio", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,"Error Conexión", Toast.LENGTH_LONG).show();
        }
    }
}
