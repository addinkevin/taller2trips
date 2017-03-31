package com.example.pc.myapplication.ciudadTools;

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

import com.example.pc.myapplication.AtraccionActivity;
import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccImg;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadAtracc;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.atraccionesTools.AtraccionesListAdp;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;
import java.util.List;

public class AtraccionesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<Atraccion> atraccionItems;
    private AtraccionesListAdp atraccionesAdp;
    private Ciudad ciudad;
    private ReceiverOnCiudadAtracc onCiudadAtracc;
    private ReceiverOnAtraccImg onAtraccImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_atracciones, container, false);

        if ( onCiudadAtracc == null) {
            atraccionItems = new ArrayList<>();
            atraccionesAdp = new AtraccionesListAdp(this,atraccionItems);

            onCiudadAtracc = new ReceiverOnCiudadAtracc(this, myFragmentView, atraccionItems);
            onAtraccImg = new ReceiverOnAtraccImg(atraccionItems,atraccionesAdp);

            String url = ((TripTP)getActivity().getApplication()).getUrl() + Consts.ATRACC + "?" + Consts.ID_CIUDAD + "=" + ciudad._id;

            InternetClient client = new InfoClient(getContext(), myFragmentView,
                    Consts.GET_CITY_ATR, url, null, Consts.GET, null, true);
            client.runInBackground();
        } else {
            atraccionesAdp = new AtraccionesListAdp(this, atraccionItems);
        }
        ListView atraccList = (ListView) myFragmentView.findViewById(R.id.atraccList);
        atraccList.setAdapter(atraccionesAdp);
        atraccList.setOnItemClickListener(this);
        return myFragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent atraccion = new Intent(getActivity(), AtraccionActivity.class);
        atraccion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        atraccion.putExtra(Consts._ID, atraccionItems.get(position)._id);
        this.startActivity(atraccion);
    }

    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadAtracc,
                new IntentFilter(Consts.GET_CITY_ATR));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onAtraccImg,
                new IntentFilter(Consts.GET_ATR_IMG));
    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadAtracc);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onAtraccImg);
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
}