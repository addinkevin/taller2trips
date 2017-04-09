package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;


public class ReceiverOnVidThumbnail extends BroadcastReceiver {
    private View view;
    private StringBuilder heightVideo;
    private StringBuilder weigthVideo;
    private Context context;

    public ReceiverOnVidThumbnail(View img, StringBuilder heightVideo, StringBuilder weigthVideo, Context context) {
        this.view = img;
        this.heightVideo = heightVideo;
        this.weigthVideo = weigthVideo;
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean success = intent.getBooleanExtra(Consts.SUCESS,false);
        View relative = view.findViewById(R.id.relativeVideo);
        TextView noVid = (TextView) view.findViewById(R.id.noVideo);
        if (success) {
            Bitmap thumbnailVideo = intent.getParcelableExtra(Consts.IMG_OUT);
            String height = intent.getStringExtra(Consts.IMG_H);
            String weight = intent.getStringExtra(Consts.IMG_W);
            if (thumbnailVideo != null) {
                ImageView img = (ImageView) view.findViewById(R.id.videoIMG);
                img.setImageBitmap(thumbnailVideo);
                heightVideo.append(height);
                weigthVideo.append(weight);
            } else {
                relative.setVisibility(View.GONE);
                noVid.setVisibility(View.VISIBLE);
                //Toast.makeText(this.context,"Error Video sin metadata.", Toast.LENGTH_LONG).show();
            }
        } else {
            relative.setVisibility(View.GONE);
            noVid.setVisibility(View.VISIBLE);
        }
    }
}
