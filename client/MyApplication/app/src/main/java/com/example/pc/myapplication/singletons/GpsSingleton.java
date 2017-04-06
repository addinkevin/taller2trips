package com.example.pc.myapplication.singletons;

import com.google.android.gms.maps.model.LatLng;

public class GpsSingleton {

    private GpsSingleton(){}
    private LatLng pos;

    private static class SingletonHelper{
        private static final GpsSingleton INSTANCE = new GpsSingleton();
    }

    public static GpsSingleton getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public void setPos(LatLng pos) {
        this.pos = pos;
    }

    public LatLng getPos() {
        if (pos == null) {
            return new LatLng(-34,-58);
        }
        return pos;
    }

}
