package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
                    String userID = res.getString(Consts._ID);
                    tripTP.setUserID_fromServ(userID);
                    tripTP.setLogin(true);
                    if (!userID.isEmpty()) {
                        String url = tripTP.getUrl() + Consts.USUARIO + "/" + userID + Consts.TOKEN;
                        JSONObject json = new JSONObject();
                        try {
                            json.put(Consts.TOKEN_PUSH, tripTP.getTokenFCM());
                            Map<String,String> headers = Consts.getHeaderJSON();

                            InternetClient client = new InfoClient(activity.getApplicationContext(),
                                    Consts.GET_CITY_REC, url, headers, Consts.PUT, json.toString(), false);
                            client.createAndRunInBackground();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
