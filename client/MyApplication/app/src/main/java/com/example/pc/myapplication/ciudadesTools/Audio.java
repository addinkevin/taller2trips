package com.example.pc.myapplication.ciudadesTools;

import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONException;
import org.json.JSONObject;

public class Audio {
    public String audioPath;
    public String idioma;

    public Audio(String audioPath, String idioma) {
        this.audioPath = audioPath;
        this.idioma = idioma;
    }

    public Audio(JSONObject jsonObject) throws JSONException {
        audioPath = jsonObject.getString(Consts.AUDIO_K);
        idioma = jsonObject.getString(Consts.IDIOMA);
    }
}
