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
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadAtrVisit;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadAtrVisitDelete;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnVisitAtraccion;
import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VisitadoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Atraccion> atraccionItems;
    private AtraccionesListAdp atraccionesAdp;
    private ReceiverOnVisitAtraccion onVisitAtraccion;
    private ReceiverOnCiudadAtrVisitDelete onCiudadAtraccVisitDelete;
    private ReceiverOnCiudadAtrVisit onCiudadAtraccVisit;
    private ReceiverOnAtraccImg onAtraccImg;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitado);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle(R.string.visitados);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        atraccionItems = new ArrayList<>();
        atraccionesAdp = new AtraccionesListAdp(this, atraccionItems);
        atraccionesAdp.setOnlyFavs(false);

        onVisitAtraccion = new ReceiverOnVisitAtraccion(this, atraccionesAdp);
        onAtraccImg = new ReceiverOnAtraccImg(atraccionesAdp);
        onCiudadAtraccVisit = new ReceiverOnCiudadAtrVisit(this, atraccionesAdp);
        onCiudadAtraccVisitDelete = new ReceiverOnCiudadAtrVisitDelete(atraccionesAdp);

        ListView atraccList = (ListView) findViewById(R.id.visitAtr);
        atraccList.setAdapter(atraccionesAdp);
        atraccList.setOnItemClickListener(this);

        TripTP tripTP = (TripTP)getApplication();

        String url = tripTP.getUrl() + Consts.VISITADO + Consts.BUSCAR
                + "?" + Consts.ID_USER + "=" + tripTP.getUserID_fromServ();

        Map<String,String> headres = Consts.getHeaderPaginadoGrande("0");

        InternetClient client = new InfoClient(getApplicationContext(),
                Consts.GET_VISIT_ATR, url, headres, Consts.GET, null, true);
        client.createAndRunInBackground();
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onVisitAtraccion,
                new IntentFilter(Consts.GET_VISIT_ATR));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtraccImg,
                new IntentFilter(Consts.GET_ATR_IMG));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onCiudadAtraccVisit,
                new IntentFilter(Consts.GEToPOST_ATR_VISIT));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onCiudadAtraccVisitDelete,
                new IntentFilter(Consts.DELETE_ATR_VISIT));
        super.onStart();
    }

    public void onStop() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onVisitAtraccion);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccImg);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onCiudadAtraccVisit);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onCiudadAtraccVisitDelete);


        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent atraccion = new Intent(this, AtraccionActivity.class);
        atraccion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        atraccion.putExtra(Consts._ID, atraccionItems.get(position)._id);
        atraccion.putExtra(Consts.ATR_RECORRIBLE, atraccionItems.get(position).isRecorrible());
        this.startActivity(atraccion);
    }
}
