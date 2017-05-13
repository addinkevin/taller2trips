package com.example.pc.myapplication;

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
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadRecFav;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadRecFavDelete;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadRecImg;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadRecorrido;
import com.example.pc.myapplication.adapters.RecorridosListAdp;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;
import java.util.List;

public class CiudadRecorridosTab extends Fragment implements AdapterView.OnItemClickListener {

    private Ciudad ciudad;
    private TripTP tripTP;
    private ReceiverOnCiudadRecorrido onCiudadRec;
    private List<Recorrido> recorridoList;
    private ReceiverOnCiudadRecImg onCiudadRecImg;
    private ReceiverOnCiudadRecFav onCiudadRecFav;
    private ReceiverOnCiudadRecFavDelete onCiudadRecFavDel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.ciudad_recorridos_tab, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(Consts.CITY)) {
            ciudad = savedInstanceState.getParcelable(Consts.CITY);
        }

        tripTP = (TripTP)getActivity().getApplication();
        recorridoList = new ArrayList<>();
        RecorridosListAdp recorridosAdp = new RecorridosListAdp(getActivity(),recorridoList);
        onCiudadRec = new ReceiverOnCiudadRecorrido(getActivity(), recorridosAdp);
        onCiudadRecImg = new ReceiverOnCiudadRecImg(recorridosAdp);
        onCiudadRecFav = new ReceiverOnCiudadRecFav(recorridosAdp);
        onCiudadRecFavDel = new ReceiverOnCiudadRecFavDelete(recorridosAdp);

        String url = tripTP.getUrl() + Consts.RECORRIDO + "?" + Consts.ID_CIUDAD + "=" + ciudad._id;

        InternetClient client = new InfoClient(getActivity().getApplicationContext(),
                Consts.GET_CITY_REC, url, null, Consts.GET, null, true);
        client.runInBackground();

        ListView atraccList = (ListView) myFragmentView.findViewById(R.id.recList);
        atraccList.setAdapter(recorridosAdp);
        atraccList.setOnItemClickListener(this);

        return myFragmentView;
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadRec,
                new IntentFilter(Consts.GET_CITY_REC));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadRecImg,
                new IntentFilter(Consts.GET_REC_FIRST_ATR_IMG));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadRecFav,
                new IntentFilter(Consts.GEToPOST_REC_FAV));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadRecFavDel,
                new IntentFilter(Consts.DELETE_REC_FAV));

        super.onStart();
    }

    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadRec);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadRecImg);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadRecFav);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadRecFavDel);
        super.onStop();
    }



    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (ciudad != null) {
            savedInstanceState.putParcelable(Consts.CITY, ciudad);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (recorridoList.get(position).hasPhoto()) {
            Intent recAct = new Intent(getActivity(), RecorridoActivity.class);
            recAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            recAct.putExtra(Consts.ID_RECORRIDO, recorridoList.get(position));
            this.startActivity(recAct);
        }
    }
}