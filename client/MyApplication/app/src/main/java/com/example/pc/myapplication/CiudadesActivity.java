package com.example.pc.myapplication;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnCiudades;
import com.example.pc.myapplication.commonfunctions.Consts;

public class CiudadesActivity extends AppCompatActivity {

    private ReceiverOnCiudades onCiudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudades);
        View view  = findViewById(R.id.ciudadesV);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onCiudades,
                new IntentFilter(Consts.GET_CITY_NAME));

        InternetClient client = new InternetClient(getApplicationContext(), view,
                Consts.GET_CITY_NAME, Consts.SERVER_URL, null, Consts.GET, null, true);
        client.runInBackground();

    }
}
