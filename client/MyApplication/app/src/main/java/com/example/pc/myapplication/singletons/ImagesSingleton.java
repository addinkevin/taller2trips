package com.example.pc.myapplication.singletons;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class ImagesSingleton {

    private static ImagesSingleton Singleton = null;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private int currentPosition;

    public static ImagesSingleton getInstance() {
        if(Singleton == null)
        {
            Singleton = new ImagesSingleton();
        }
        return Singleton;
    }

    public int size() {
        return bitmaps.size();
    }

    public Bitmap get(int i) {
        return bitmaps.get(i);
    }

    public void add(Bitmap bitmap) {
        bitmaps.add(bitmap);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrent(int current) {
        this.currentPosition = current;
    }

    public void clear() {
        bitmaps.clear();
    }
}
