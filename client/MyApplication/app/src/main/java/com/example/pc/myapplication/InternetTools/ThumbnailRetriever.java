package com.example.pc.myapplication.InternetTools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;

import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.HashMap;

public class ThumbnailRetriever extends AsyncTask<String, Void, Integer> {

    private String toCall;
    private Context context;

    public ThumbnailRetriever(String toCall, Context context) {
        this.toCall = toCall;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String videoPath = params[0];
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Intent activityMsg = new Intent(toCall);
        boolean success = true;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            success = false;
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }

        activityMsg.putExtra(Consts.SUCESS, success);
        if (success) {
            activityMsg.putExtra(Consts.IMG_OUT, bitmap);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(activityMsg);

        return 1;
    }
}
