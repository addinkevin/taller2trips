package com.example.pc.myapplication.commonfunctions;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;

import java.util.HashMap;

public class Consts {

    public static final String EXT = ".mp4";


    //Request methods
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    // Internet Client keys
    public static final String JSON_OUT = "JSON";
    public static final String IMG_OUT = "IMG";
    public static final String FILE_OUT = "FILE";
    public static final String IMG_ID = "IDIMG";
    public static final String SUCESS = "SUCESS";
    public final static String RESULT = "RESULT";

    //Receiver tags
    public static final String GET_CITY_NAME = "NAMECITY";
    public static final String GET_CITY_IMG = "IMGCITY";
    public static final String GET_CITY_ATR = "ATRCITY";
    public static final String GET_ATR = "_ATR";
    public static final String GET_ATR_IMG = "IMGATR";// una sola imagen, la primera
    public static final String GET_ATR_IMG_S = "IMGsATR"; //todas las imagenes
    public static final String GET_ATR_VID = "VIDATR"; //cuando hace click en video
    public static final String GET_VID_THU = "VIDTHU"; //cuando hace click en video

    //public server
    public static  final String SERVER_URL = "http://192.168.0.18:3000/api";
    //path
    public static final String CIUDAD = "/ciudad";
    public static final String IMAGEN = "/imagen";
    public static final String AUDIO = "/audio";
    public static final String VIDEO = "/video";
    public static final String ATRACC = "/atraccion";

    //Json Keys
    public static final String NOMBRE = "nombre";
    public static final String DESCRIPCION = "descripcion";
    public static final String _ID = "_id";
    public static final String PAIS = "pais";
    public static final String COSTO = "costo";
    public static final String RATING = "rating";
    public static final String CANT_VOTOS = "cantidad_votos";
    public static final String HS_APERTURA = "hora_apertura";
    public static final String HS_CIERRE = "hora_cierre";
    public static final String DURACION = "duracion";
    public static final String CLASIFICACION = "clasificacion";
    public static final String PLANO = "plano";
    public static final String ID_CIUDAD = "id_ciudad";
    public static final String FOTOS = "imagenes";
    public static final String VIDEO_K = "video";
    public static final String AUDIOS = "audios";
    public static final String AUDIO_K = "AUDIO_K";
    public static final String IDIOMA = "idioma";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";

    //querry param
    public static final String FILENAME = "filename";

    //sharedPreferences keys
    public static final String URL = "url";

    //savedIntance tags
    public static final String CITY = "ciudad";

    public static final int CANT_STARS = 5;





}
