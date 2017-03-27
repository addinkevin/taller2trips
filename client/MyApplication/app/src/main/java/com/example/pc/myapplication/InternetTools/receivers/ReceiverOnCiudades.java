package com.example.pc.myapplication.InternetTools.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.CiudadActivity;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.commonfunctions.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CompletionService;

public class ReceiverOnCiudades extends BroadcastReceiver {

    private Context ctx;
    private AutoCompleteTextView autoTxtV;
    private List<Ciudad> ciudades;

    public ReceiverOnCiudades(Context applicationContext, AutoCompleteTextView autoTxtV, List<Ciudad> ciudades) {
        this.ctx = applicationContext;
        this.autoTxtV = autoTxtV;
        this.ciudades = ciudades;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean succes = intent.getBooleanExtra(Consts.SUCESS, false);
        if (succes) {
            String jsonOut = intent.getStringExtra(Consts.JSON_OUT);
            if (jsonOut != null) {
                try {
                    JSONArray jsonA = new JSONArray(jsonOut);

                    for (int i = 0; i < jsonA.length(); i++) {
                        JSONObject jsonO = jsonA.getJSONObject(i);
                        Ciudad ciudad = new Ciudad(jsonO.getString(Consts.NOMBRE),
                                jsonO.getString(Consts.DESCRIPCION),
                                jsonO.getString(Consts.PAIS),
                                jsonO.getString(Consts._ID));
                        ciudades.add(ciudad);

                    }
                    ArrayAdapter<Ciudad> adapter = new ArrayAdapter<>(context,
                            R.layout.dropdown, ciudades);
                    autoTxtV.setAdapter(adapter);
                    autoTxtV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Ciudad ciudad = (Ciudad)parent.getAdapter().getItem(position);
                            Intent chat = new Intent(context, CiudadActivity.class);
                            chat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            chat.putExtra(Consts._ID, ciudad._id);
                            chat.putExtra(Consts.NOMBRE, ciudad.nombre);
                            chat.putExtra(Consts.DESCRIPCION, ciudad.descripcion);
                            chat.putExtra(Consts.PAIS, ciudad.pais);
                            context.startActivity(chat);

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
