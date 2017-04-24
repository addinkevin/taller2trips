package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

public class Comentario {
    public Bitmap profPic = null;
    public String userName;
    public String comment;
    public String idSocial;
    public String provider;
    public int calificacion;
    public boolean finish;

    public Comentario(JSONObject res) throws JSONException {
        comment = res.getString(Consts.DESCRIPCION);
        calificacion = res.getInt(Consts.CALIFICACION);
        idSocial = res.getString(Consts.ID_USER_SOCIAL);
        provider = res.getString(Consts.PROVIDER);
        userName = res.getString(Consts.NAME);
        finish = false;
    }
}
