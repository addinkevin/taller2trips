package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;

/**
 * Created by PC on 19/05/2017.
 */

public class ReceiverOnAudCheckPI extends BroadcastReceiver {
    private View view;

    public ReceiverOnAudCheckPI(View view) {

        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (!succes) {
            view.findViewById(R.id.audioLL).setVisibility(View.GONE);
            view.findViewById(R.id.noAudio).setVisibility(View.VISIBLE);
        }
    }
}
