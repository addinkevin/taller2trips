package com.example.pc.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pc.myapplication.InternetTools.ReadMapTask;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.mapTools.MapInfoWindowAdapter;
import com.example.pc.myapplication.singletons.GpsSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private float latitud;
    private float longitud;
    private LinearLayout mapLN;
    private String descripcion;
    private String nombre;

    private void hideClockBateryBar(){
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        hideClockBateryBar();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);

        mapLN = (LinearLayout) findViewById(R.id.mapLN);

        latitud = getIntent().getFloatExtra(Consts.LATITUD,0f);
        longitud = getIntent().getFloatExtra(Consts.LONGITUD,0f);
        descripcion = getIntent().getStringExtra(Consts.DESCRIPCION);
        nombre = getIntent().getStringExtra(Consts.NOMBRE);
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



    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(latitud, longitud);
        googleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(getLayoutInflater()));
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        googleMap.addMarker(new MarkerOptions()
                .title(nombre)
                .snippet(descripcion)
                .position(latLng)
                .flat(true))
                .setTag(0);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

        String url = getMapsApiDirectionsUrl(GpsSingleton.getInstance().getPos(), latLng);
        ReadMapTask downloadTask = new ReadMapTask(googleMap, getApplicationContext());
        downloadTask.execute(url);


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent imageFull = new Intent(this, FullImageMapActivity.class);
        imageFull.putExtra(Consts.DESCRIPCION,marker.getSnippet());
        imageFull.putExtra(Consts.NOMBRE,marker.getTitle());
        imageFull.putExtra(Consts.POS,marker.getPosition());
        imageFull.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(imageFull);
    }
}

