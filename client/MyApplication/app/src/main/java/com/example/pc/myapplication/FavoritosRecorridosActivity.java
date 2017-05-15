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
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadRecFav;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadRecFavDelete;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadRecImg;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadRecorrido;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnFavRecorrido;
import com.example.pc.myapplication.adapters.RecorridosListAdp;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;
import java.util.Map;

public class FavoritosRecorridosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<Recorrido> recorridoList;
    private ReceiverOnFavRecorrido onCiudadRec;
    private ReceiverOnCiudadRecImg onCiudadRecImg;
    private ReceiverOnCiudadRecFav onCiudadRecFav;
    private ReceiverOnCiudadRecFavDelete onCiudadRecFavDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito_recorrido);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle(R.string.recorridos);
        setSupportActionBar(toolbar);

        recorridoList = new ArrayList<>();
        RecorridosListAdp recorridosAdp = new RecorridosListAdp(this, recorridoList);
        onCiudadRec = new ReceiverOnFavRecorrido(this, recorridosAdp);
        onCiudadRecImg = new ReceiverOnCiudadRecImg(recorridosAdp);
        onCiudadRecFav = new ReceiverOnCiudadRecFav(recorridosAdp);
        onCiudadRecFavDel = new ReceiverOnCiudadRecFavDelete(recorridosAdp);

        TripTP tripTP = (TripTP)getApplication();

        String url = tripTP.getUrl() + Consts.FAVS + Consts.BUSCAR
                + "?" + Consts.ID_USER + "=" + tripTP.getUserID_fromServ();

        Map<String,String> headres = Consts.getHeaderPaginadoTipoBusqueda("0", Consts.TIPO_BUSQ_REC);

        InternetClient client = new InfoClient(getApplicationContext(),
                Consts.GET_FAV_REC, url, headres, Consts.GET, null, true);
        client.createAndRunInBackground();

        ListView atraccList = (ListView) findViewById(R.id.favRec);
        atraccList.setAdapter(recorridosAdp);
        atraccList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (recorridoList.get(position).hasPhoto()) {
            Intent recAct = new Intent(this, RecorridoActivity.class);
            recAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            recAct.putExtra(Consts.ID_RECORRIDO, recorridoList.get(position));
            this.startActivity(recAct);
        }
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onCiudadRec,
                new IntentFilter(Consts.GET_FAV_REC));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onCiudadRecImg,
                new IntentFilter(Consts.GET_REC_FIRST_ATR_IMG));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onCiudadRecFav,
                new IntentFilter(Consts.GEToPOST_REC_FAV));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onCiudadRecFavDel,
                new IntentFilter(Consts.DELETE_REC_FAV));

        super.onStart();
    }

    public void onStop() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onCiudadRec);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onCiudadRecImg);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onCiudadRecFav);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onCiudadRecFavDel);
        super.onStop();
    }
}
