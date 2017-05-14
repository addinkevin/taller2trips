package com.example.pc.myapplication;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudades;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Ciudad;
import com.example.pc.myapplication.commonfunctions.Consts;

import java.util.ArrayList;
import java.util.List;

public class CiudadesActivity extends AppCompatActivity {

    private ReceiverOnCiudades onCiudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudades);

        AutoCompleteTextView autoTxtV = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        List<Ciudad> ciudades = new ArrayList<>();
        onCiudades = new ReceiverOnCiudades(getApplicationContext(),autoTxtV,ciudades);

        InternetClient client = new InfoClient(getApplicationContext(),
                Consts.GET_CITY_NAME, ((TripTP)getApplication()).getUrl() + Consts.CIUDAD, null, Consts.GET, null, true);
        client.createAndRunInBackground();
    }

    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onCiudades,
                new IntentFilter(Consts.GET_CITY_NAME));
    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onCiudades);
    }
}
