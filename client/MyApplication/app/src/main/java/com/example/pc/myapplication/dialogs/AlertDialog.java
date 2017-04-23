package com.example.pc.myapplication.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

public class AlertDialog {

    public static void show(final Activity act,int message) {
        show(act,message, false);
    }

    public static void show(final Activity act, int message, final boolean finish) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(act);
        builder.setTitle(R.string.app_name);
        builder.setMessage(message)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (finish) {
                            act.finish();
                        }
                    }
                });

        builder.create().show();
    }
}
