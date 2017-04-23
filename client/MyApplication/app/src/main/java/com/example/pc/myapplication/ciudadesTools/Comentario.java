package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

public class Comentario {
    public Bitmap profPic = null;
    public String userName = "Sebastian Rocha";
    public String comment;
    public int calificacion;

    public Comentario(JSONObject res) throws JSONException {
        comment = res.getString(Consts.DESCRIPCION);
        calificacion = res.getInt(Consts.CALIFICACION);
    }
}
