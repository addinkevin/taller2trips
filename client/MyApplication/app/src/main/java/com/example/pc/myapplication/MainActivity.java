package com.example.pc.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtrNearInMap;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnUserAccounts;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnUserImage;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnUserInfo;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnUserLogin;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.mapTools.MapInfoWindowAdapter;
import com.example.pc.myapplication.services.LocationGPSListener;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.twitter.sdk.android.Twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnInfoWindowClickListener {

    private LocationManager locationManager;
    private LocationListener listener;
    private ReceiverOnAtrNearInMap onArtNearInMap;
    private List<Atraccion> atraccionItems;

    private Toolbar toolbar;
    private View view;
    private ReceiverOnUserAccounts onUserAccounts;
    private ReceiverOnUserInfo onUserInfo;
    private ReceiverOnUserImage onUserImage;
    private ReceiverOnUserLogin onUserLogin;
    private TripTP tripTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle("Taller2Trips");
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        view = findViewById(R.id.mainLayout);
        atraccionItems = new ArrayList<>();
        onArtNearInMap = new ReceiverOnAtrNearInMap(atraccionItems);
        onUserAccounts = new ReceiverOnUserAccounts(this,headerView,view);
        onUserInfo = new ReceiverOnUserInfo(this);
        onUserImage = new ReceiverOnUserImage(this,headerView);
        onUserLogin = new ReceiverOnUserLogin(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(onUserAccounts,
                new IntentFilter(Consts.GET_USER_ACCOUNTS));
        LocalBroadcastManager.getInstance(this).registerReceiver(onUserInfo,
                new IntentFilter(Consts.GET_USER_INFO));
        LocalBroadcastManager.getInstance(this).registerReceiver(onUserImage,
                new IntentFilter(Consts.GET_USER_IMG));
        LocalBroadcastManager.getInstance(this).registerReceiver(onUserLogin,
                new IntentFilter(Consts.POST_SIGNIN));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        tripTP = (TripTP) getApplication();
        Menu menu = navigationView.getMenu();
        if (!tripTP.getSocialDef().isEmpty()) {
            menu.findItem(R.id.login).setVisible(false);
            String urlSplex = Consts.SPLEX_URL + Consts.SOCIAL_ACC;

            Map<String, String> header = Consts.getSplexHeader(getApplication());

            InternetClient client = new InfoClient(getApplicationContext(),
                    Consts.GET_USER_ACCOUNTS, urlSplex, header, Consts.GET, null, true);
            client.runInBackground();
        } else {
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.link).setVisible(false);

        }
    }

    void locationConfig(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                locationConfig();
                break;
            default:
                break;
        }
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(onArtNearInMap,
                new IntentFilter(Consts.GET_ATR_CERC));
        super.onStart();
    }

    public void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onArtNearInMap);
        super.onStop();
    }

    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onUserAccounts);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onUserInfo);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onUserImage);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onUserLogin);

        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        onArtNearInMap.setMap(map);

        map.setInfoWindowAdapter(new MapInfoWindowAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);
        String url = tripTP.getUrl() + Consts.ATRACC + Consts.CERCANIA;
        listener = new LocationGPSListener(this,map, url, tripTP.getRadio(), view);
        locationConfig();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ciudades) {
            Intent i = new Intent(this, CiudadesActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.config) {
            Intent config = new Intent(this, ConfigActivity.class);
            config.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(config);
        } else if (id == R.id.buscar_cercanos) {
            Intent buscaAct = new Intent(this, BuscaAtrCercaActivity.class);
            buscaAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(buscaAct);
        } else if (id == R.id.atracciones) {
            Intent atraccionesAct = new Intent(this, AtraccionesActivity.class);
            atraccionesAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(atraccionesAct);
        } else if ( id == R.id.logout ) {
            LoginManager.getInstance().logOut();
            Twitter.logOut();
            Intent loginAct = new Intent(this, LoginActivity.class);
            loginAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginAct);
            this.finish();
        } else if (id == R.id.link){
            Intent loginAct = new Intent(this, LoginActivity.class);
            loginAct.putExtra(Consts.IS_LINKING, true);
            loginAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginAct);
        } else if (id == R.id.login) {
            Intent loginAct = new Intent(this, LoginActivity.class);
            loginAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginAct);
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
