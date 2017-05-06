package com.example.pc.myapplication.commonfunctions;

import android.app.Application;

import com.example.pc.myapplication.application.TripTP;

import java.util.HashMap;
import java.util.Map;

public class Consts {

    public static final String EXT = ".mp4";

    public static final String URL_FACEBOOK = "http://graph.facebook.com/v2.9/";
    public static final String PICTURE = "/picture";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String URL_TWITTER = "https://twitter.com/";
    public static final String PROFILE_IMAGE = "/profile_image";
    public static final String ORIGINAL = "original";
    public static final String SIZE = "size";


    //splex parameters
    public static final String SPLEX_URL = "https://api.splex.rocks";
    public static final String SIGNUP = "/signup";
    public static final String LOGIN = "/login";
    public static final String FACEBOOK = "/facebook";
    public static final String TWITTER = "/twitter";
    public static final String SOCIAL_ACC = "/social-accounts";
    public static final String POSTS = "/posts";

    public static final String SPLEX_SECRET = "7d0a3e85b63bcc4f0fffb40cf1b5c43c97fc17a67f76e4bd700a0bf2106a91f0";

    //splex headers
    public static final String AUTHORIZATION = "Authorization";

    //response server
    public static final String EXITO = "exito";

    //splex json
    public static final String DATA = "data";
    public static final String ACC_DATA = "accountData";
    public static final String SPLEX_ACCTOKEN = "splexUserToken";
    public static final String ACC_TOKEN = "accessToken";
    public static final String ACC_TOKEN_SECRET = "accessTokenSecret";
    public static final String SOCIAL = "socialAccounts";
    public static final String PROVIDER = "provider";
    public static final String PROFIMG = "profile_image";
    public static final String PROFBANN = "banner_image";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TEXT = "text";
    public static final String SOCIAL_ACCOUNTS = "socialAccounts";
    public static final String POST_JS = "post";
    public static final String PROVIDER_ACT = "providerActions";
    public static final String ERROR = "error";
    public static final String USER_ID_SPLEX = "user_id";
    public static final String S_FACEBOOK = "FACEBOOK";
    public static final String S_TWITTER = "TWITTER";

    //Facebook permissions
    public static final String PUBLISH = "publish_actions";
    public static final String GRANTED = "granted";

    //Facebook json keys
    public static final String PERMISSION = "permission";
    public static final String STATUS = "status";

    //Request methods
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    // Internet Client keys
    public static final String JSON_OUT = "JSON";
    public static final String IMG_OUT = "IMG";
    public static final String URL_OUT = "URL";
    public static final String RESPONSE = "RES";
    public static final String IMG_ID = "IDIMG";
    public static final String URL_ID = "URL_ID";
    public static final String IMG_H = "IMGH";
    public static final String IMG_W = "IMGW";
    public static final String SUCESS = "SUCESS";
    public final static String RESULT = "RESULT";

    //posicion imagenes de perfil de usuario
    public static final int PROF_IMG = 1;
    public static final int BANNER_IMG = 2;
    public static final String CANTIDAD = "6";
    public static final String S_CANTIDAD = "cantidad";
    public static final String S_SALTO = "salto";

    //Receiver tags
    public static final String GET_CITY_NAME = "NAMECITY";
    public static final String GET_CITY_IMG = "IMGCITY";
    public static final String GET_CITY_ATR = "ATRCITY";
    public static final String GET_ATR = "_ATR";
    public static final String GET_ATR_IMG = "IMGATR";// una sola imagen, la primera
    public static final String GET_ATR_IMG_S = "IMGsATR"; //todas las imagenes
    public static final String GET_ATR_PLANO = "PLANOATR"; //plano
    public static final String GET_VID_THU = "VIDTHU"; //cuando hace click en video
    public static final String GET_ATRS = "ATRS";
    public static final String GET_CHECK_VID = "VID";
    public static final String GET_CHECK_AUD = "AUD";
    public static final String GET_ATR_CERC = "CERC";
    public static final String POST_SIGN_LOGIN = "SINGLOG";
    public static final String GET_USER_INFO = "INFOUSER";
    public static final String GET_USER_ACCOUNTS = "USERACC";
    public static final String GET_USER_IMG = "USERIMG";// una sola imagen, la primera
    public static final String POST_SHARE = "SHARE";
    public static final String POST_COMMENT = "COMM";
    public static final String POST_SIGNIN = "SIGNIN";
    public static final String GET_COMMENT = "GETCOMM";
    public static final String GET_PROF = "PROFPIC";
    public static final String GET_USER_IMG_PROF = "USERIMGPROF";// una sola imagen, la primera
    public static final String GET_FAV_ATR = "FAVATR";
    public static final String GEToPOST_ATR_FAV = "ATRFAV";
    public static final String DELETE_ATR_FAV = "DELATRFAV";


    //path
    public static final String CIUDAD = "/ciudad";
    public static final String IMAGEN = "/imagen";
    public static final String AUDIO = "/audio";
    public static final String VIDEO = "/video";
    public static final String ATRACC = "/atraccion";
    public static final String CERCANIA = "/cercania";
    public static final String PLANO = "/plano";
    public static final String RESENIA = "/resenia";
    public static final String BUSCAR = "/buscar";
    public static final String SIGNIN = "/signin";
    public static final String PAGINADO = "/paginas";
    public static final String FAVS = "/favorito";

    //Json Keys
    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";
    public static final String EMAIL = "email";
    public static final String DESCRIPCION = "descripcion";
    public static final String _ID = "_id";
    public static final String ID_USER = "id_usuario";
    public static final String ID_SOCIAL = "id_social";
    public static final String ID_ATR = "id_atraccion";
    public static final String PAIS = "pais";
    public static final String COSTO = "costo_monto";
    public static final String MONEDA = "costo_moneda";
    public static final String RATING = "rating";
    public static final String CANT_VOTOS = "cantidad_votos";
    public static final String HS_APERTURA = "hora_apertura";
    public static final String HS_CIERRE = "hora_cierre";
    public static final String DURACION = "duracion";
    public static final String CLASIFICACION = "clasificacion";
    public static final String ID_CIUDAD = "id_ciudad";
    public static final String FOTOS = "imagenes";
    public static final String AUDIO_K = "AUDIO_K";
    public static final String IDIOMA = "idioma"; //tmb se usa como querry
    public static final String LATITUD = "latitud"; //tmb se usa como querry
    public static final String LONGITUD = "longitud";//tmb se usa como querry
    public static final String CALIFICACION = "calificacion";
    public static final String IS_LINKING = "isLinking";
    public static final String ID_USER_SOCIAL = "id_userSocial";

    //querry param
    public static final String FILENAME = "filename";

    //sharedPreferences keys
    public static final String URL = "url";
    public static final String SOCIAL_DEF = "socialDef";
    public static final String POS = "pos";
    public static final String RADIO = "radio";

    //savedIntance tags
    public static final String CITY = "ciudad";

    public static final int CANT_STARS = 5;
    public static final int DEF_RADIO = 25;
    public static final String DEF_IDIOMA = "en";

    public static Map<String, String> getSplexHeader(Application secret) {
       return getSplexHeader(secret, false);
    }

    public static Map<String,String> getSplexHeader(Application secret, boolean isLogin) {
        Map<String,String> header = getHeaderJSON ();
        if(isLogin) {
            header.put(Consts.AUTHORIZATION, "Bearer " + Consts.SPLEX_SECRET);
        } else {
            header.put(Consts.AUTHORIZATION, "Bearer " + ((TripTP) secret).getSplexSecret());
        }
        return header;
    }

    public static Map<String,String> getHeaderJSON () {
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json; charset=utf-8");
        return header;
    }

    public static Map<String,String> getHeaderPaginado (String salto) {
        Map<String,String> headers = new HashMap<>();
        headers.put(Consts.S_CANTIDAD,CANTIDAD);
        headers.put(Consts.S_SALTO,salto);
        return headers;
    }

}
