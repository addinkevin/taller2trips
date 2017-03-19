package com.example.pc.myapplication.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.example.pc.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationGPSListener implements LocationListener {

    private final Context ctx;
    private static final String LOCATION_TAG = "LocationGPSListener";
    private GoogleMap map;
    private MarkerOptions marker = null;

    public LocationGPSListener(Context ctx, GoogleMap map) {
        this.ctx = ctx;
        this.map = map;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng position = new LatLng(location.getLatitude(),location.getLongitude());

        if (marker == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
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

        Geocoder gcd = new Geocoder(ctx, Locale.getDefault());
        List<Address> addresses = null;
        String ciudad = null;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                ciudad = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(location.getLongitude() + " " + location.getLatitude());

        Toast.makeText(ctx, location.getLongitude() + " " + location.getLatitude() + " "+ ciudad, Toast.LENGTH_LONG).show();
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
