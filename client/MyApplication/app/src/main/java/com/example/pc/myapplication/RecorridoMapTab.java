package com.example.pc.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.myapplication.InternetTools.ReadMapTask;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.ciudadesTools.Recorrido;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.mapTools.MapInfoWindowAdapter;
import com.example.pc.myapplication.singletons.GpsSingleton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class RecorridoMapTab extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private View fragView;
    private MapView mMapView;
    private GoogleMap map;
    private Activity activity;
    private Recorrido recorrido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.recorrido_map_tab, container, false);

        activity = getActivity();

        if ( recorrido == null ) {
            recorrido = activity.getIntent().getParcelableExtra(Consts.ID_RECORRIDO);
        }

        mMapView = (MapView) fragView.findViewById(R.id.map2);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return fragView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setInfoWindowAdapter(new MapInfoWindowAdapter(activity.getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);
        if (recorrido != null && recorrido.getAtraccionSize() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < recorrido.getAtraccionSize(); i++) {
                Atraccion atraccion = recorrido.getAtraccionAt(i);
                builder.include(atraccion.getLatLng());
                setMapContent(atraccion, i);
                if (i + 1 < recorrido.getAtraccionSize()) {
                    Atraccion atraccionNext = recorrido.getAtraccionAt(i + 1);
                    traceRoute(atraccion.getLatLng(), atraccionNext.getLatLng());
                }
            }
            CameraUpdate cu;
            if (recorrido.getAtraccionSize() > 1 ) {
                LatLngBounds bounds = builder.build();
                cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            } else {
                Atraccion atraccion = recorrido.getFirstAtraccion();
               cu = CameraUpdateFactory.newLatLngZoom(atraccion.getLatLng(), 12.0f);
            }

            map.animateCamera(cu);
        }
    }

    private void traceRoute(LatLng ini, LatLng fin) {
        String url = getMapsApiDirectionsUrl(ini, fin);
        ReadMapTask downloadTask = new ReadMapTask(map, activity.getApplicationContext());
        downloadTask.execute(url);
    }

    private String  getMapsApiDirectionsUrl(LatLng origin,LatLng dest) {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private void setMapContent(Atraccion atraccion, int i) {
        LatLng latLng = new LatLng(atraccion.latitud, atraccion.longitud);
        map.addMarker(new MarkerOptions()
                .title(atraccion.nombre)
                .snippet(atraccion.descripcion)
                .position(latLng)
                .flat(true))
                .setTag(i);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent imageFull = new Intent(activity, FullImageMapActivity.class);
        imageFull.putExtra(Consts.DESCRIPCION,marker.getSnippet());
        imageFull.putExtra(Consts.NOMBRE,marker.getTitle());
        imageFull.putExtra(Consts.POS,marker.getPosition());
        imageFull.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(imageFull);
    }
}
