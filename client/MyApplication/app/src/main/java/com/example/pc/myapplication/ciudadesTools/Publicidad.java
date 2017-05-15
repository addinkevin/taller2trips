package com.example.pc.myapplication.ciudadesTools;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.pc.myapplication.commonfunctions.Consts;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

public class Publicidad implements Parcelable {
    private String link;
    private String _id;
    private String descripcion;
    private String nombre;
    private Bitmap imagen;

    public Publicidad(RemoteMessage.Notification data) {
        try {
            JSONObject json = new JSONObject(data.getBody());
            String idioma = null;
            try {
                idioma = URLEncoder.encode(Locale.getDefault().getLanguage().toLowerCase(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                JSONObject descrip = json.getJSONObject(Consts.DESCRIPCION);
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

            link = json.getString(Consts.LINK);
            nombre = data.getTitle();
            _id = json.getString(Consts._ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Publicidad(Parcel in) {

        String[] data = new String[4];
        in.readStringArray(data);

        //en orden de write to parcel
        _id =  data[0];
        nombre =  data[1];
        descripcion =  data[2];
        link =  data[3];
    }

    public Publicidad(Map<String, String> data) {

            String idioma = null;
            try {
                idioma = URLEncoder.encode(Locale.getDefault().getLanguage().toLowerCase(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                JSONObject descrip = new JSONObject(data.get(Consts.DESCRIPCION));
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

            link = data.get(Consts.LINK);
            nombre = data.get(Consts.NOMBRE);
            _id =  data.get(Consts._ID).replace("\"", "");


    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static final Creator<Publicidad> CREATOR = new Creator<Publicidad>() {
        @Override
        public Publicidad createFromParcel(Parcel in) {
            return new Publicidad(in);
        }

        @Override
        public Publicidad[] newArray(int size) {
            return new Publicidad[size];
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
                link});
    }


    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
