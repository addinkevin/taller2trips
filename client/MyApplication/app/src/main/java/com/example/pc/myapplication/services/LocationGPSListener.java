package com.example.pc.myapplication.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.R;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.singletons.GpsSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationGPSListener implements LocationListener {

    private final Context ctx;
    private static final String LOCATION_TAG = "LocationGPSListener";
    private GoogleMap map;
    private String urlToAtr;
    private Integer radio;
    private View view;
    private MarkerOptions marker = null;
    private boolean firstLocation = true;

    public LocationGPSListener(Context ctx, GoogleMap map, String urlToAtr, Integer radio, View view) {
        this.ctx = ctx;
        this.map = map;
        this.urlToAtr = urlToAtr;
        this.radio = radio;
        this.view = view;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng position = new LatLng(location.getLatitude(),location.getLongitude());
        GpsSingleton.getInstance().setPos(position);

        if (marker == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, getZoomLevel(radio)));
            marker = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_simple))
                    .position(position)
                    .flat(true)
                    .rotation(location.getBearing()-45);
            map.addMarker(marker);
        } else {
            marker.position(position)
                    .rotation(location.getBearing()-45);
        }

        if (firstLocation) {
            firstLocation = false;
            urlToAtr = urlToAtr + "?" + Consts.LATITUD + "=" + position.latitude +
                    "&" + Consts.LONGITUD + "=" + position.longitude +
                    "&" + Consts.RADIO + "=" + radio;
            InternetClient client = new InfoClient(ctx.getApplicationContext(),
                    Consts.GET_ATR_CERC, urlToAtr, null, Consts.GET, null, true);
            client.createAndRunInBackground();
        }

    }

    public int getZoomLevel(int radio) {

        double scale = radio*1000 / 500;

        return (int) (16 - Math.log(scale) / Math.log(2));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}
    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        ((Activity)ctx).startActivity(i);
    }
}
