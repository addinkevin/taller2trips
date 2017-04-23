package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiverOnShare extends BroadcastReceiver {

    private Context ctx;

    public ReceiverOnShare(Context context) {
        this.ctx = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONArray response = new JSONObject(jsonOut)
                            .getJSONObject(Consts.DATA)
                            .getJSONObject(Consts.POST_JS)
                            .getJSONArray(Consts.PROVIDER_ACT);

                    int i = 0;
                    boolean hasErr = false;
                    while ( i < response.length() && !hasErr) {
                        JSONObject socialResponse = response.getJSONObject(i);
                        if (socialResponse.has(Consts.ERROR)) {
                            hasErr = true;
                            Toast.makeText(ctx,"No se pudo compartir el contenido.", Toast.LENGTH_LONG).show();
                        }
                        i++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(ctx,"No hay respuesta del servidor", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(ctx,"No se pudo conectar con Splex", Toast.LENGTH_LONG).show();
        }

    }
}
