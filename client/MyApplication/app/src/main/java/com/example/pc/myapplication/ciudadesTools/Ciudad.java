package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class Ciudad implements Parcelable {
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

    public Ciudad(JSONObject ciudad) throws JSONException {
        this.nombre = ciudad.getString(Consts.NOMBRE);

        String idioma = null;
        try {
            idioma = URLEncoder.encode(Locale.getDefault().getLanguage().toLowerCase(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONArray descrip = ciudad.getJSONArray(Consts.DESCRIPCION);
        if (idioma != null) {
            int i = 0;
            boolean found = false;
            while(i < descrip.length() && !found) {
                if (descrip.getJSONObject(i).has(idioma)) {
                    descripcion = descrip.getJSONObject(i).getString(idioma);
                    found = true;
                }
                i++;
            }

            getDefIdioma(descrip);
        } else {
            getDefIdioma(descrip);
        }

        if (descripcion == null) {
            descripcion = "";
        }

        this.pais = ciudad.getString(Consts.PAIS);
        this._id = ciudad.getString(Consts._ID);

    }

    private void getDefIdioma(JSONArray descrip) throws JSONException {
        int i = 0;
        boolean found = false;
        while(i < descrip.length() && !found) {
            if (descrip.getJSONObject(i).has(Consts.DEF_IDIOMA)) {
                descripcion = descrip.getJSONObject(i).getString(Consts.DEF_IDIOMA);
                found = true;
            }
            i++;
        }
    }


    protected Ciudad(Parcel in) {

        String[] data = new String[3];

        in.readStringArray(data);
        //en orden de write to parcel
        nombre = data[1];
        descripcion = data[2];
        pais = data[3];
        _id = data[0];
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Ciudad> CREATOR = new Creator<Ciudad>() {
        @Override
        public Ciudad createFromParcel(Parcel in) {
            return new Ciudad(in);
        }

        @Override
        public Ciudad[] newArray(int size) {
            return new Ciudad[size];
        }
    };

    public String toString() {
        return nombre + " (" + pais + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this._id,
                this.nombre,
                this.descripcion,
                this.pais});
        dest.writeParcelable(imagen,flags);
    }
}
