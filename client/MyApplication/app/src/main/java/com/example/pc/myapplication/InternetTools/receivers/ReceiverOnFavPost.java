package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.dialogs.AlertDialog;

public class ReceiverOnFavPost extends BroadcastReceiver {

    private Activity act;

    public ReceiverOnFavPost(Activity act) {
        this.act = act;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
        int id = intent.getIntExtra(Consts.URL_ID, -1);
        if (succes && jsonOut != null) {
            AlertDialog.show(act, R.string.fav_post_succ);
        } else {
            AlertDialog.show(act, R.string.fav_post_err);
        }
    }
}
