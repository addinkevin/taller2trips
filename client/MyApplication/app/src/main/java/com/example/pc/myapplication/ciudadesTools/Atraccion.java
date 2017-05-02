package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by PC on 26/03/2017.
 */

public class Atraccion {

    public String _id;
    public String nombre;
    public String descripcion;
    public float costo;
    public float rating;
    public int cantVotos;
    public String horaApert;
    public String horaCierre;
    public float duracion;
    public String clasificacion;
    public String idCiudad;
    public List<String> fotosPath = new ArrayList<>();
    public List<Bitmap> fotosBitmap = new ArrayList<>();
    public float latitud;
    public float longitud;
    public String moneda;

    public Atraccion(JSONObject jsonO) throws JSONException {
        this._id = jsonO.getString(Consts._ID);
        this.nombre = jsonO.getString(Consts.NOMBRE);

        String idioma = null;
        try {
            idioma = URLEncoder.encode(Locale.getDefault().getLanguage().toLowerCase(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject descrip = jsonO.getJSONObject(Consts.DESCRIPCION);
            if (idioma != null) {
                if (descrip.has(idioma)) {
                    descripcion = descrip.getString(idioma);

                } else if (descrip.has(Consts.DEF_IDIOMA)) {
                    descripcion = descrip.getString(Consts.DEF_IDIOMA);
                } else {
                    descripcion = "";
                }
            } else {
                descripcion = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
             descripcion = "";
        }

        this.moneda = jsonO.getString(Consts.MONEDA);
        this.costo = (float) jsonO.getDouble(Consts.COSTO);
        this.rating = (float) jsonO.getDouble(Consts.RATING);

        this.cantVotos = jsonO.getInt(Consts.CANT_VOTOS);
        this.horaApert = jsonO.getString(Consts.HS_APERTURA);
        this.horaCierre = jsonO.getString(Consts.HS_CIERRE);
        this.duracion = (float) jsonO.getDouble(Consts.DURACION);
        this.clasificacion = jsonO.getString(Consts.CLASIFICACION);
        this.idCiudad = jsonO.getString(Consts.ID_CIUDAD);
        JSONArray jsonA = jsonO.getJSONArray(Consts.FOTOS);
        for(int i = 0; i < jsonA.length(); i++) {
            this.fotosPath.add((String) jsonA.get(i));
        }

        latitud = (float) jsonO.getDouble(Consts.LATITUD);
        longitud = (float) jsonO.getDouble(Consts.LONGITUD);
    }
}
