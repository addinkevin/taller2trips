package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.pc.myapplication.commonfunctions.Consts;
import com.google.android.gms.maps.model.LatLng;

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

public class Atraccion implements Parcelable{

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
    private Boolean fav;
    private Boolean visit;
    private String id_fav;
    private String id_visit;
    private boolean recorrible;
    private List<PuntoInteres> puntosInteres = new ArrayList<>();

    public Atraccion(JSONObject jsonO) throws JSONException {
        this(jsonO, false);
    }

    public Atraccion(JSONObject jsonO, boolean withPuntos) throws JSONException {
        this._id = jsonO.getString(Consts._ID);
        this.nombre = jsonO.getString(Consts.NOMBRE);
        fav = null;
        id_fav = null;
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

        try {
            recorrible = jsonO.getBoolean(Consts.RECORRIBLE);
        } catch (JSONException e) {
            recorrible = false;
        }

        if (recorrible && withPuntos) {
            JSONArray jsonP = jsonO.getJSONArray(Consts.IDS_PUNTOS_INTERES);
            for(int i = 0; i < jsonP.length(); i++) {
                this.puntosInteres.add(new PuntoInteres(jsonP.getJSONObject(i)));
            }
        }
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

    public boolean isVisitSetted() {
        return visit != null;
    }

    public String getId_fav() {
        return id_fav;
    }

    public void setId_fav(String id_fav) {
        this.id_fav = id_fav;
    }

    protected Atraccion(Parcel in) {

        String[] data = new String[9];
        float[] position = new float[2];
        boolean[] recorrible = new boolean[1];

        in.readStringArray(data);
        in.readFloatArray(position);
        in.readStringList(fotosPath);
        in.readBooleanArray(recorrible);
        //en orden de write to parcel
        _id =  data[0];
        nombre =  data[1];
        descripcion =  data[2];
        horaApert =  data[3];
        horaCierre =  data[4];
        clasificacion =  data[5];
        idCiudad =  data[6];
        moneda =  data[7];
        id_fav =  data[8];
        latitud = position[0];
        longitud = position[1];
        this.recorrible = recorrible[0];
    }

    public static final Creator<Atraccion> CREATOR = new Creator<Atraccion>() {
        @Override
        public Atraccion createFromParcel(Parcel in) {
            return new Atraccion(in);
        }

        @Override
        public Atraccion[] newArray(int size) {
            return new Atraccion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {_id,
                        nombre,
                        descripcion,
                        horaApert,
                        horaCierre,
                        clasificacion,
                        idCiudad,
                        moneda,
                        id_fav,});
        dest.writeFloatArray(new float[] {
                        latitud,
                        longitud});
        dest.writeStringList(fotosPath);
        dest.writeBooleanArray(new boolean[]{recorrible});
    }

    public LatLng getLatLng() {
        return new LatLng(latitud, longitud);
    }

    public Boolean isVisit() {
        if (visit == null) {
            return false;
        }
        return visit;
    }

    public void setIsVisit(Boolean visit) {
        this.visit = visit;
    }

    public String getId_visit() {
        return id_visit;
    }

    public void setId_visit(String id_visit) {
        this.id_visit = id_visit;
    }

    public boolean isRecorrible() {
        return recorrible;
    }

    public void setRecorrible(boolean recorrible) {
        this.recorrible = recorrible;
    }

    public List<PuntoInteres> getPuntosInteres() {
        return puntosInteres;
    }
}
