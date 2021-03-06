package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.singletons.NetClientsSingleton;

public class ReceiverOnProfPic extends BroadcastReceiver {

    private Activity activity;

    public ReceiverOnProfPic(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String urlProfPic = intent.getStringExtra(Consts.URL_OUT);
        Integer identifier = intent.getIntExtra(Consts.URL_ID,-1);
        if (urlProfPic != null && identifier >= 0 ) {
            InternetClient profPic = new ImageClient(activity.getApplicationContext(),
                    Consts.GET_USER_IMG_PROF, urlProfPic, null, Consts.GET, null, true, identifier);
            NetClientsSingleton.getInstance().add(profPic.createTask());
            profPic.runInBackground();
        }
    }

}
