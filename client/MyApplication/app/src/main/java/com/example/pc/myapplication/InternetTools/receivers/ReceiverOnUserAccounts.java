package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ReceiverOnUserAccounts extends BroadcastReceiver {

    private Activity activity;
    private View headerView;
    private View view;

    public ReceiverOnUserAccounts(Activity mainActivity, View headerView, View view) {
        this.activity = mainActivity;
        this.headerView = headerView;
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonOut);

                    JSONArray accounts = jsonObject.getJSONObject(Consts.DATA).getJSONArray(Consts.SOCIAL);
                    TripTP tripTP = (TripTP)activity.getApplication();
                    String socialDef = tripTP.getSocialDef();
                    int i = 0;
                    JSONObject socialDefJson = null;
                    boolean notFound = true;
                    while (i < accounts.length() && notFound) {
                        if (accounts.getJSONObject(i).getString(Consts.PROVIDER).equals(socialDef)) {
                            notFound = false;
                            socialDefJson = accounts.getJSONObject(i);
                        } else {
                            i++;
                        }
                    }

                    tripTP.setHasMultipleAccounts(accounts.length() > 1);

                    if (socialDefJson != null) {
                        String id = socialDefJson.getString(Consts.ID);
                        tripTP.setSocialDefID(Integer.parseInt(id));
                        JSONObject data = socialDefJson.getJSONObject(Consts.DATA);
                        String name = data.getString(Consts.NAME);
                        tripTP.setUserID_fromSocial(data.getString(Consts.USER_ID_SPLEX));

                        TextView nameTxt = (TextView) headerView.findViewById(R.id.nameProf);
                        nameTxt.setText(name);

                        String url = Consts.SPLEX_URL + Consts.SOCIAL_ACC + "/" + id;
                        Map<String,String> header = Consts.getSplexHeader(tripTP);

                        InternetClient client = new InfoClient(activity.getApplicationContext(),
                                Consts.GET_USER_INFO, url, header, Consts.GET, null, true);
                        client.runInBackground();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"JSON err", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity,"JSON err", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity,"No se pudo conectar con Splex", Toast.LENGTH_LONG).show();
        }

    }
}
