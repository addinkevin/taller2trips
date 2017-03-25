package com.example.pc.myapplication.InternetTools.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiverOnCiudades extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            try {
                JSONObject jsonO = new JSONObject(jsonOut);
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
