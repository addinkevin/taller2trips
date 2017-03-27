package com.example.pc.myapplication.InternetTools.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.io.File;
import java.io.IOException;


public class ReceiverOnAtraccionVid extends BroadcastReceiver {

    private AtraccionActivity atraccionActivity;

    public ReceiverOnAtraccionVid(AtraccionActivity atraccionActivity) {
        this.atraccionActivity = atraccionActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String path = intent.getStringExtra(Consts.FILE_OUT);
            File f = new File(path);
            if (!path.isEmpty() && f.exists()) {

                String extension = MimeTypeMap.getFileExtensionFromUrl(f.getAbsolutePath());
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
               // mediaIntent.setData(Uri.fromFile(f));
                mediaIntent.setDataAndType(Uri.fromFile(f), mimeType);
                atraccionActivity.startActivity(mediaIntent);


            }
        }
    }
}
