package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PC on 22/04/2017.
 */

public class ReceiverOnUserLogin extends BroadcastReceiver {

    private final TripTP tripTP;
    private Activity activity;

    public ReceiverOnUserLogin(Activity mainActivity) {
        this.activity = mainActivity;
        tripTP = (TripTP)activity.getApplication();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONObject res = new JSONObject(jsonOut);
                    tripTP.setUserID_fromServ(res.getString(Consts._ID));
                    tripTP.setLogin(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"Json err", Toast.LENGTH_LONG).show();
                    tripTP.setLogin(false);
                }
            } else {
                Toast.makeText(activity,"No se pudo conectar sign servidor", Toast.LENGTH_LONG).show();
                tripTP.setLogin(false);
            }
        } else {
            Toast.makeText(activity,"No se pudo conectar con el servidor", Toast.LENGTH_LONG).show();
            tripTP.setLogin(false);
        }
    }
}
