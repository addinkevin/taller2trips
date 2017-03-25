package com.example.pc.myapplication.atraccionesTools;

import android.graphics.Bitmap;

public class AtraccionItem {

    public String _id;
    public String atraccionName;
    public Bitmap atraccionPic;

    public AtraccionItem(String atraccionName, Bitmap atraccionPic, String _id) {
        this._id = _id;
        this.atraccionName = atraccionName;
        this.atraccionPic = atraccionPic;
    }
}