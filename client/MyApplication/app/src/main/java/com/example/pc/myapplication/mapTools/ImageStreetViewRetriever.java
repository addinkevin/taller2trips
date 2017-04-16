package com.example.pc.myapplication.mapTools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;


public abstract class ImageStreetViewRetriever extends AsyncTask<String, Void, Bitmap> {

    protected Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            return BitmapFactory.decodeStream((InputStream)url.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
