package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;

/**
 * Created by PC on 29/03/2017.
 */

public class ReceiverOnVidThumbnail extends BroadcastReceiver {
    private ImageView img;
    private StringBuilder heightVideo;
    private StringBuilder weigthVideo;
    private Context context;

    public ReceiverOnVidThumbnail(ImageView img, StringBuilder heightVideo, StringBuilder weigthVideo, Context context) {
        this.img = img;
        this.heightVideo = heightVideo;
        this.weigthVideo = weigthVideo;
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean success = intent.getBooleanExtra(Consts.SUCESS,false);
        if (success) {
            Bitmap thumbnailVideo = intent.getParcelableExtra(Consts.IMG_OUT);
            String height = intent.getStringExtra(Consts.IMG_H);
            String weight = intent.getStringExtra(Consts.IMG_W);
            if (thumbnailVideo != null) {
                img.setImageBitmap(thumbnailVideo);
                heightVideo.append(height);
                weigthVideo.append(weight);
            } else {
                Toast.makeText(this.context,"Error Video sin metadata.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
