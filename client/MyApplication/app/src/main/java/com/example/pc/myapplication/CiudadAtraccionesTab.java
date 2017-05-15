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
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;
import java.util.List;

public class CiudadAtraccionesTab extends Fragment implements AdapterView.OnItemClickListener {

    private List<Atraccion> atraccionItems;
    private AtraccionesListAdp atraccionesAdp;
    private Ciudad ciudad;
    private ReceiverOnCiudadAtracc onCiudadAtracc;
    private ReceiverOnAtraccImg onAtraccImg;
    private ReceiverOnCiudadAtraccFav onCiudadAtraccFav;
    private ReceiverOnCiudadAtraccFavDelete onCiudadAtraccFavDelete;
    private Activity activity;
    private ReceiverOnCiudadAtrVisit onCiudadAtraccVisit;
    private ReceiverOnCiudadAtrVisitDelete onCiudadAtraccVisitDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.ciudad_atracciones_tab, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(Consts.CITY)) {
            ciudad = savedInstanceState.getParcelable(Consts.CITY);
        }

        activity = getActivity();

        if ( onCiudadAtracc == null) {
            atraccionItems = new ArrayList<>();
            atraccionesAdp = new AtraccionesListAdp(activity,atraccionItems);

            onCiudadAtracc = new ReceiverOnCiudadAtracc(activity, atraccionesAdp);
            onAtraccImg = new ReceiverOnAtraccImg(atraccionesAdp);
            onCiudadAtraccFav = new ReceiverOnCiudadAtraccFav(atraccionesAdp, activity);
            onCiudadAtraccFavDelete = new ReceiverOnCiudadAtraccFavDelete(atraccionesAdp);
            onCiudadAtraccVisit = new ReceiverOnCiudadAtrVisit(activity, atraccionesAdp);
            onCiudadAtraccVisitDelete = new ReceiverOnCiudadAtrVisitDelete(atraccionesAdp);

            String url = ((TripTP)activity.getApplication()).getUrl() + Consts.ATRACC + "?" + Consts.ID_CIUDAD + "=" + ciudad._id;

            InternetClient client = new InfoClient(activity.getApplicationContext(),
                    Consts.GET_CITY_ATR, url, null, Consts.GET, null, true);
            client.createAndRunInBackground();
        } else {
            atraccionesAdp = new AtraccionesListAdp(activity, atraccionItems);
        }
        ListView atraccList = (ListView) myFragmentView.findViewById(R.id.atraccList);
        atraccList.setAdapter(atraccionesAdp);
        atraccList.setOnItemClickListener(this);

        return myFragmentView;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (ciudad != null) {
            savedInstanceState.putParcelable(Consts.CITY, ciudad);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent atraccion = new Intent(activity, AtraccionActivity.class);
        atraccion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        atraccion.putExtra(Consts._ID, atraccionItems.get(position)._id);
        this.startActivity(atraccion);
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadAtracc,
                new IntentFilter(Consts.GET_CITY_ATR));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onAtraccImg,
                new IntentFilter(Consts.GET_ATR_IMG));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadAtraccFav,
                new IntentFilter(Consts.GEToPOST_ATR_FAV));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadAtraccVisit,
                new IntentFilter(Consts.GEToPOST_ATR_VISIT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadAtraccFavDelete,
                new IntentFilter(Consts.DELETE_ATR_FAV));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadAtraccVisitDelete,
                new IntentFilter(Consts.DELETE_ATR_VISIT));
        super.onStart();
    }

    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadAtracc);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onAtraccImg);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadAtraccFav);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadAtraccFavDelete);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadAtraccVisit);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadAtraccVisitDelete);


        super.onStop();
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
}