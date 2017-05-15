package com.example.pc.myapplication.InternetTools.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pc.myapplication.CiudadAtraccionesTab;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PC on 14/05/2017.
 */

public class ReceiverOnCiudadAtrVisit extends BroadcastReceiver {

    private Activity act;
    private AtraccionesListAdp atraccionesAdp;

    public ReceiverOnCiudadAtrVisit(Activity act, AtraccionesListAdp atraccionesAdp) {
        this.act = act;
        this.atraccionesAdp = atraccionesAdp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
        int id = intent.getIntExtra(Consts.URL_ID, -1);
        try {
            JSONArray atrVisit = new JSONArray(jsonOut);
            if (atrVisit.length() > 0) {
                setIsVisitAndIdVisit(id,succes && jsonOut != null,atrVisit.getJSONObject(0));
            } else {
                setIsVisit(id,false);
            }
        } catch (JSONException e) {
            try {
                JSONObject atrFav = new JSONObject(jsonOut);
                setIsVisitAndIdVisit(id,succes && jsonOut != null, atrFav);
                Toast.makeText(act, R.string.addToVisit, Toast.LENGTH_SHORT).show();
            } catch (JSONException e1) {
                setIsVisit(id,false);
            }
        }
        atraccionesAdp.notifyDataSetChanged();

    }

    private void setIsVisit(int id, boolean isVisit){
        atraccionesAdp.setIsVisit(id, isVisit);
        atraccionesAdp.setNeedUpdateToPos(id, true);
    }

    private void setIsVisitAndIdVisit(int id, boolean isVisit, JSONObject visit) throws JSONException {
        setIsVisit(id,isVisit);
        if ( isVisit && visit != null) {
            atraccionesAdp.setId_visit(id, visit.getString(Consts._ID));
        }
    }
}
