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
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadAtracc;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadAtraccFav;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadAtraccFavDelete;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.adapters.AtraccionesListAdp;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;
import java.util.List;

public class AtraccionesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Atraccion> atraccionItems;
    private AtraccionesListAdp atraccionesAdp;
    private ReceiverOnCiudadAtracc onCiudadAtracc;
    private ReceiverOnAtraccImg onAtraccImg;
    private ReceiverOnCiudadAtraccFav onCiudadAtraccFav;
    private ReceiverOnCiudadAtraccFavDelete onCiudadAtraccFavDelete;
    private ReceiverOnCiudadAtrVisit onCiudadAtraccVisit;
    private ReceiverOnCiudadAtrVisitDelete onCiudadAtraccVisitDelete;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atracciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle(R.string.atracciones);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        View view = findViewById(R.id.buscaAtr);

        TripTP tripTP = (TripTP) getApplication();

        atraccionItems = new ArrayList<>();
        atraccionesAdp = new AtraccionesListAdp(this, atraccionItems);
        onCiudadAtracc = new ReceiverOnCiudadAtracc(this, atraccionesAdp);
        onAtraccImg = new ReceiverOnAtraccImg(atraccionesAdp);
        onCiudadAtraccFav = new ReceiverOnCiudadAtraccFav(atraccionesAdp, this);
        onCiudadAtraccFavDelete = new ReceiverOnCiudadAtraccFavDelete(atraccionesAdp);
        onCiudadAtraccVisit = new ReceiverOnCiudadAtrVisit(this, atraccionesAdp);
        onCiudadAtraccVisitDelete = new ReceiverOnCiudadAtrVisitDelete(atraccionesAdp);


        ListView atraccList = (ListView) findViewById(R.id.atraccList);
        atraccList.setAdapter(atraccionesAdp);
        atraccList.setOnItemClickListener(this);

        String url = tripTP.getUrl() + Consts.ATRACC;
        InternetClient client = new InfoClient(getApplicationContext(),
                Consts.GET_ATRS, url, null, Consts.GET, null, true);
        client.createAndRunInBackground();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent atraccion = new Intent(this, AtraccionActivity.class);
        atraccion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        atraccion.putExtra(Consts._ID, atraccionItems.get(position)._id);
        this.startActivity(atraccion);
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(onCiudadAtracc,
                new IntentFilter(Consts.GET_ATRS));
        LocalBroadcastManager.getInstance(this).registerReceiver(onAtraccImg,
                new IntentFilter(Consts.GET_ATR_IMG));
        LocalBroadcastManager.getInstance(this).registerReceiver(onCiudadAtraccFav,
                new IntentFilter(Consts.GEToPOST_ATR_FAV));
        LocalBroadcastManager.getInstance(this).registerReceiver(onCiudadAtraccVisit,
                new IntentFilter(Consts.GEToPOST_ATR_VISIT));
        LocalBroadcastManager.getInstance(this).registerReceiver(onCiudadAtraccFavDelete,
                new IntentFilter(Consts.DELETE_ATR_FAV));
        LocalBroadcastManager.getInstance(this).registerReceiver(onCiudadAtraccVisitDelete,
                new IntentFilter(Consts.DELETE_ATR_VISIT));
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onCiudadAtracc);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onAtraccImg);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onCiudadAtraccFav);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onCiudadAtraccFavDelete);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onCiudadAtraccVisit);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onCiudadAtraccVisitDelete);
    }
}
