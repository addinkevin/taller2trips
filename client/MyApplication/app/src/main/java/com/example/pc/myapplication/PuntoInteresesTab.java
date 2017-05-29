package com.example.pc.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionPlano;
import com.example.pc.myapplication.adapters.PuntosInteresAdp;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.PuntoInteres;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.singletons.NetClientsSingleton;
import com.example.pc.myapplication.singletons.PosVideoSingleton;

import java.util.List;

public class PuntoInteresesTab extends Fragment {

    private View fragView;
    private Activity activity;
    private Atraccion atraccion;
    private String _id;
    private ReceiverOnAtraccionPlano onAtrPlano;
    private TripTP tripTP;
    private ListView puntosInteresLV;
    private List<PuntoInteres> puntosInteresList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.punto_intereses_tab, container, false);
        activity = getActivity();

        _id = activity.getIntent().getStringExtra(Consts._ID);

        tripTP = (TripTP) activity.getApplication();

        onAtrPlano = new ReceiverOnAtraccionPlano(fragView);

        puntosInteresLV = (ListView) fragView.findViewById(R.id.recList);
        puntosInteresLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PosVideoSingleton.getInstance().clear();
                Intent piAct = new Intent(activity, PuntoInteresActivity.class);
                piAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                piAct.putExtra(Consts._ID, puntosInteresList.get(position)._id);
                startActivity(piAct);
            }
        });

        if (atraccion != null) {
            PuntosInteresAdp puntosInteresAdp = new PuntosInteresAdp(activity,atraccion.getPuntosInteres());
            puntosInteresList = atraccion.getPuntosInteres();
            puntosInteresLV.setAdapter(puntosInteresAdp);
        }

        String urlPlano = tripTP.getUrl() + Consts.ATRACC + "/" + _id + Consts.PLANO;
        InternetClient client = new ImageClient(activity.getApplicationContext(),
                Consts.GET_ATR_PLANO, urlPlano, null, Consts.GET, null, true, -1);
        NetClientsSingleton.getInstance().add(client.createTask());
        client.runInBackground();

        return fragView;
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onAtrPlano,
                new IntentFilter(Consts.GET_ATR_PLANO));
        super.onStart();
    }

    public void onStop() {
        unregister();
        super.onStop();
    }
    public void unregister() {
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onAtrPlano);
    }

    //Dependiendo del momento en que se llame puede estar creado o no puntosInteresLV
    public void attachAtraccion(Atraccion atraccion) {
        this.atraccion = atraccion;
        if (puntosInteresLV != null) {
            PuntosInteresAdp puntosInteresAdp = new PuntosInteresAdp(activity, atraccion.getPuntosInteres());
            puntosInteresList = atraccion.getPuntosInteres();
            puntosInteresLV.setAdapter(puntosInteresAdp);
        }
    }
}
