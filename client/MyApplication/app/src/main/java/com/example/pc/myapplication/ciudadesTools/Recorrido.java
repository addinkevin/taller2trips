package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 07/05/2017.
 */

public class Recorrido implements Parcelable{
    private String _id;
    private String nombre;
    private String descripcion;
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
            descripcion = jsonRec.getString(Consts.DESCRIPCION);
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

    public Atraccion getAtraccionAt(int index) {
        return atracciones.get(index);
    }

    public int getAtraccionSize() {
        return atracciones.size();
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

    public String getDescripcion() {
        return descripcion;
    }

    protected Recorrido(Parcel in) {

        String[] data = new String[3];

        in.readStringArray(data);
        //en orden de write to parcel
        _id = data[0];
        nombre = data[1];
        descripcion = data[2];
        id_ciudad = data[3];
        id_fav = data[4];
        fotoRecorrido = in.readParcelable(Bitmap.class.getClassLoader());
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
                this.id_ciudad,
                id_fav});
        dest.writeParcelable(fotoRecorrido, flags);
        dest.writeTypedList(atracciones);
    }

}
