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

public class PuntoInteres implements Parcelable {

    public String _id;
    public String nombre;
    public String descripcion;

    public List<String> fotosPath = new ArrayList<>();
    public List<Bitmap> fotosBitmap = new ArrayList<>();

    private Boolean fav;
    private Boolean visit;
    private String id_fav;
    private String id_visit;

    public PuntoInteres(JSONObject jsonO) throws JSONException {
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

        JSONArray jsonA = jsonO.getJSONArray(Consts.FOTOS);
        for(int i = 0; i < jsonA.length(); i++) {
            this.fotosPath.add((String) jsonA.get(i));
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

    protected PuntoInteres(Parcel in) {

        String[] data = new String[4];

        in.readStringArray(data);
        in.readStringList(fotosPath);
        //en orden de write to parcel
        _id =  data[0];
        nombre =  data[1];
        descripcion =  data[2];
        id_fav =  data[3];

    }

    public static final Creator<PuntoInteres> CREATOR = new Creator<PuntoInteres>() {
        @Override
        public PuntoInteres createFromParcel(Parcel in) {
            return new PuntoInteres(in);
        }

        @Override
        public PuntoInteres[] newArray(int size) {
            return new PuntoInteres[size];
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
                id_fav,});
        dest.writeStringList(fotosPath);
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
}
