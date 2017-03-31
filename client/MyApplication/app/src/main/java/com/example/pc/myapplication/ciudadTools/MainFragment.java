package com.example.pc.myapplication.ciudadTools;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.myapplication.InternetTools.ImageClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudadImage;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.commonfunctions.Consts;

public class MainFragment extends Fragment {

    private Ciudad ciudad;
    private ReceiverOnCiudadImage onCiudadImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_ciudad, container, false);

        if ( onCiudadImage == null || ciudad.imagen == null) {
            onCiudadImage = new ReceiverOnCiudadImage(ciudad, myFragmentView);

            String url = ((TripTP)getActivity().getApplication()).getUrl() + Consts.CIUDAD + "/" + ciudad._id + Consts.IMAGEN;

            InternetClient client = new ImageClient(getContext(), myFragmentView,
                    Consts.GET_CITY_IMG, url, null, Consts.GET, null, true, -1);
            client.runInBackground();
        } else {
            ImageView img = (ImageView) myFragmentView.findViewById(R.id.imageTitle);
            img.setImageBitmap(ciudad.imagen);
        }

            TextView cityTxt = (TextView) myFragmentView.findViewById(R.id.cityTxt);
            cityTxt.setText(ciudad.nombre);

            TextView paisTxt = (TextView) myFragmentView.findViewById(R.id.countryTxt);
            paisTxt.setText(ciudad.pais);

            TextView descripTxt = (TextView) myFragmentView.findViewById(R.id.infoText);
            descripTxt.setText(ciudad.descripcion);

        return myFragmentView;
    }

    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onCiudadImage,
                new IntentFilter(Consts.GET_CITY_IMG));
    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onCiudadImage);
    }

    public void setCiudad (Ciudad ciudad) {
        this.ciudad = ciudad;
    }
}