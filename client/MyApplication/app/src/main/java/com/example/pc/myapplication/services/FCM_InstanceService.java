package com.example.pc.myapplication.services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class FCM_InstanceService extends FirebaseInstanceIdService {

    private static final String TAG = FCM_InstanceService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        TripTP tripTP = (TripTP)getApplication();

        tripTP.setTokenFCM(refreshedToken);

        String userID = tripTP.getLastUserLogged();
        if (!userID.isEmpty()) {
            String url = tripTP.getUrl() + Consts.USUARIO + "/" + userID + Consts.TOKEN;
            JSONObject json = new JSONObject();
            try {
                json.put(Consts.TOKEN_PUSH, refreshedToken);
                Map<String,String> headers = Consts.getHeaderJSON();

                InternetClient client = new InfoClient(getApplicationContext(),
                        Consts.GET_CITY_REC, url, headers, Consts.PUT, json.toString(), false);
                client.createAndRunInBackground();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
