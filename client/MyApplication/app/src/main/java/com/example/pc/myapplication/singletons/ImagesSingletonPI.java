package com.example.pc.myapplication.singletons;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 19/05/2017.
 */

public class ImagesSingletonPI {
    private static ImagesSingletonPI Singleton = null;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private int currentPosition;

    public static ImagesSingletonPI getInstance() {
        if(Singleton == null)
        {
            Singleton = new ImagesSingletonPI();
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

    public boolean isEmpty() {
        return bitmaps.isEmpty();
    }
}
