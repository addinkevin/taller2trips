package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.pc.myapplication.InternetTools.ThumbnailRetriever;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;

public class ReceiverOnVidCheck extends BroadcastReceiver {

    private View view;
    private String urlConst;

    public ReceiverOnVidCheck(View view, String urlConst) {
        this.view = view;
        this.urlConst = urlConst;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (!succes) {
            view.findViewById(R.id.relativeVideo).setVisibility(View.GONE);
            view.findViewById(R.id.noVideo).setVisibility(View.VISIBLE);
        } else {
            String url = urlConst + Consts.VIDEO;
            new ThumbnailRetriever(Consts.GET_VID_THU, context).execute(url);
        }
    }
}
