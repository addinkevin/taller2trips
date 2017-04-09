package com.example.pc.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccImg;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadAtracc;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.atraccionesTools.AtraccionesListAdp;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.singletons.GpsSingleton;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class BuscaAtrCercaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Atraccion> atraccionItems;
    private AtraccionesListAdp atraccionesAdp;
    private ReceiverOnCiudadAtracc onCiudadAtracc;
    private ReceiverOnAtraccImg onAtraccImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_atr_cerca);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle(R.string.cercanoTitle);
        setSupportActionBar(toolbar);

        View view = findViewById(R.id.buscaAtr);

        LatLng pos = GpsSingleton.getInstance().getPos();
        TripTP tripTP = (TripTP) getApplication();

        atraccionItems = new ArrayList<>();
        atraccionesAdp = new AtraccionesListAdp(this, atraccionItems);
        onCiudadAtracc = new ReceiverOnCiudadAtracc(this, view, atraccionItems);
        onAtraccImg = new ReceiverOnAtraccImg(atraccionItems, atraccionesAdp);


        ListView atraccList = (ListView) findViewById(R.id.atraccList);
        atraccList.setAdapter(atraccionesAdp);
        atraccList.setOnItemClickListener(this);

        String url = tripTP.getUrl() + Consts.ATRACC + Consts.CERCANIA +
                "?" + Consts.LATITUD + "=" + pos.latitude +
                "&" + Consts.LONGITUD + "=" + pos.longitude +
                "&" + Consts.RADIO + "=" + tripTP.getRadio();
        InternetClient client = new InfoClient(this, view,
                Consts.GET_ATR_CERC, url, null, Consts.GET, null, true);
        client.runInBackground();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent atraccion = new Intent(this, AtraccionActivity.class);
        atraccion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        atraccion.putExtra(Consts._ID, atraccionItems.get(position)._id);
        this.startActivity(atraccion);
    }

    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(onCiudadAtracc,
                new IntentFilter(Consts.GET_ATR_CERC));
        LocalBroadcastManager.getInstance(this).registerReceiver(onAtraccImg,
                new IntentFilter(Consts.GET_ATR_IMG));
    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onCiudadAtracc);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onAtraccImg);
    }

}
