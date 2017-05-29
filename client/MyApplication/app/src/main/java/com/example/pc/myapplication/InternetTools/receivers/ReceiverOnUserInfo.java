package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

public class ReceiverOnUserInfo extends BroadcastReceiver {

    private Activity activity;

    public ReceiverOnUserInfo(Activity mainActivity) {

        activity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonOut);
                    JSONObject jsonData = jsonObject.getJSONObject(Consts.DATA).getJSONObject(Consts.ACC_DATA);

                    TripTP tripTP = (TripTP) activity.getApplication();

                    JSONObject reqServ = new JSONObject();
                    String[] nombre = jsonData.getString(Consts.NAME).split(" ");
                    reqServ.put(Consts.NOMBRE,nombre[0]);
                    if (nombre.length > 1)
                        reqServ.put(Consts.APELLIDO, nombre[1]);
                    else
                        reqServ.put(Consts.APELLIDO, "");
                    reqServ.put(Consts.ID_SOCIAL,tripTP.getSocialDefID());
                    if (reqServ.has(Consts.EMAIL))
                        reqServ.put(Consts.EMAIL, jsonData.getString(Consts.EMAIL));
                    reqServ.put(Consts.PROVIDER, tripTP.getSocialDef());
                    TelephonyManager tm = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
                    String countryCode = tm.getSimCountryIso();
                    Locale loc = new Locale("",countryCode);
                    String countryName = loc.getDisplayCountry();
                    reqServ.put(Consts.PAIS,countryName);

                    String ulrServ = tripTP.getUrl() + Consts.SIGNIN;
                    Map<String,String> header = Consts.getSplexHeader(tripTP);

                    InternetClient client = new InfoClient(activity.getApplicationContext(),
                            Consts.POST_SIGNIN, ulrServ, header, Consts.POST, reqServ.toString(), true);
                    client.createAndRunInBackground();

                    String urlProfPic = jsonData.getString(Consts.PROFIMG);
                    InternetClient profPic = new ImageClient(activity.getApplicationContext(),
                            Consts.GET_USER_IMG, urlProfPic, null, Consts.GET, null, true, Consts.PROF_IMG);
                    profPic.createAndRunInBackground();

                    if (jsonData.has(Consts.PROFBANN)) {
                        String urlBannerPic = jsonData.getString(Consts.PROFBANN);
                        InternetClient bannerPic = new ImageClient(activity.getApplicationContext(),
                                Consts.GET_USER_IMG, urlBannerPic, null, Consts.GET, null, true, Consts.BANNER_IMG);
                        bannerPic.createAndRunInBackground();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity,"JSON err", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity,"No se pudo conectar con Splex", Toast.LENGTH_LONG).show();
        }

    }
}
