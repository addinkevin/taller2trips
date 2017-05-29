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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Recorrido implements Parcelable{
    private String _id;
    private String nombre;
    private String descripcion;
    private Ciudad ciudad;
    private Bitmap fotoRecorrido;
    private List<Atraccion> atracciones;
    private Boolean fav;
    private String id_fav;


    public Recorrido(JSONObject jsonRec) {
        try {
            _id = jsonRec.getString(Consts._ID);
            nombre = jsonRec.getString(Consts.NOMBRE);
            ciudad = new Ciudad(jsonRec.getJSONObject(Consts.ID_CIUDAD));
            String idioma = null;
            try {
                idioma = URLEncoder.encode(Locale.getDefault().getLanguage().toLowerCase(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                JSONObject descrip = jsonRec.getJSONObject(Consts.DESCRIPCION);
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
            atracciones = new ArrayList<>();
            fotoRecorrido = null;

            JSONArray recAtr = jsonRec.getJSONArray(Consts.IDS_ATRACCIONES);

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

    public Atraccion getAtraccionAt(int index) {
        return atracciones.get(index);
    }

    public int getAtraccionSize() {
        return atracciones.size();
    }

    public Atraccion getFirstAtraccion() {
        if (atracciones.isEmpty()) {
            return null;
        }
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
        return ciudad._id;
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

    public String getDescripcion() {
        return descripcion;
    }

    protected Recorrido(Parcel in) {

        String[] data = new String[4];
        atracciones = new ArrayList<>();

        in.readStringArray(data);
        //en orden de write to parcel
        _id = data[0];
        nombre = data[1];
        descripcion = data[2];
        id_fav = data[3];
        ciudad = in.readParcelable(Ciudad.class.getClassLoader());
        in.readTypedList(atracciones, Atraccion.CREATOR);
    }

    public static final Creator<Recorrido> CREATOR = new Creator<Recorrido>() {
        @Override
        public Recorrido createFromParcel(Parcel in) {
            return new Recorrido(in);
        }

        @Override
        public Recorrido[] newArray(int size) {
            return new Recorrido[size];
        }
    };

    public String toString() {
        return nombre;
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
                id_fav});
        dest.writeParcelable(ciudad, flags);
        dest.writeTypedList(atracciones);
    }

}
