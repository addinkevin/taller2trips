package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

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
                this.pais});
        dest.writeParcelable(imagen,flags);
    }
}
