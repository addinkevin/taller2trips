package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;

public class Ciudad {
    public String nombre;
    public String descripcion;
    public String pais;
    public String _id;
    public Bitmap imagen;

    public Ciudad(String nombre, String descripcion, String pais, String _id) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pais = pais;
        this._id = _id;
    }

    public String toString() {
        return nombre;
    }
}
