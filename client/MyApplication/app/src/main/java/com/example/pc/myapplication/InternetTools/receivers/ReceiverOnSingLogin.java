package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.MainActivity;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Publicidad;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;


public class ReceiverOnSingLogin extends BroadcastReceiver {

    private final Activity activity;
    private ProgressDialog progressDialog;
    private boolean isLinking;
    private TripTP app;

    public ReceiverOnSingLogin(Activity activity, ProgressDialog progressDialog, boolean isLinking) {
        this.app = (TripTP) activity.getApplication();
        this.activity = activity;
        this.progressDialog = progressDialog;
        this.isLinking = isLinking;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    if (!isLinking) {
                        JSONObject jsonObject = new JSONObject(jsonOut);
                        String token = jsonObject.getJSONObject(Consts.DATA).getString(Consts.SPLEX_ACCTOKEN);
                        app.setSplexSecret(token);
                        goHomeActivity();
                    } else {
                        AlertDialog.show(activity, R.string.link_succes, true);
                        app.setHasMultipleAccounts(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    response("JSON err");
                }
            } else {
                response("JSON err");
            }
        } else {
            response("No se pudo conectar con Splex");
        }

        progressDialog.dismiss();
    }

    private void response (String message) {
        if (!isLinking) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        } else {
            app.setHasMultipleAccounts(false);
            AlertDialog.show(activity, R.string.link_fail);
        }
    }

    private void goHomeActivity() {
        Intent i = new Intent(activity, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        boolean hasPublicidad = activity.getIntent().getBooleanExtra(Consts.HAS_PUBLICIDAD, false);
        if (hasPublicidad) {
            Publicidad publi = activity.getIntent().getParcelableExtra(Consts.PUBLICIDAD);
            i.putExtra(Consts.PUBLICIDAD,publi);
            i.putExtra(Consts.HAS_PUBLICIDAD, true);
        }
        activity.startActivity(i);
        activity.finish();
    }
}
