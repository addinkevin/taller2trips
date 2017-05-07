package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 07/05/2017.
 */

public class Recorrido {
    private String _id;
    private String nombre;
    private String id_ciudad;
    private Bitmap fotoRecorrido;
    private List<Atraccion> atracciones;
    private Boolean fav;
    private String id_fav;


    public Recorrido(JSONObject jsonRec) {
        try {
            _id = jsonRec.getString(Consts._ID);
            nombre = jsonRec.getString(Consts.NOMBRE);
            id_ciudad = jsonRec.getString(Consts.ID_RECORRIDO);
            atracciones = new ArrayList<>();
            fotoRecorrido = null;

            JSONArray recAtr = jsonRec.getJSONArray(Consts.RECORRIDO_ATR);


            for (int i = 0; i < recAtr.length(); i++) {
                Atraccion atraccion = new Atraccion(recAtr.getJSONObject(i));
                atracciones.add(atraccion);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String get_id() {
        return _id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Atraccion> getAtracciones() {
        return atracciones;
    }

    public Atraccion getFirstAtraccion() {
        return atracciones.get(0);
    }

    public void setIsFav(Boolean fav) {
        this.fav = fav;
    }

    public boolean isFav() {
        if (fav == null) {
            return false;
        }
        return fav;
    }

    public boolean isFavSetted() {
        return fav != null;
    }

    public String getId_fav() {
        return id_fav;
    }

    public void setId_fav(String id_fav) {
        this.id_fav = id_fav;
    }


    public String getId_ciudad() {
        return id_ciudad;
    }

    public Bitmap getFotoRecorrido() {
        return fotoRecorrido;
    }

    public void setFotoRecorrido(Bitmap fotoRecorrido) {
        this.fotoRecorrido = fotoRecorrido;
    }

    public boolean hasPhoto() {
        return fotoRecorrido != null;
    }
}
