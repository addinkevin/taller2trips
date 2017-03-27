package com.example.pc.myapplication;

import android.content.IntentFilter;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.VideoClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccion;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionImgs;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionVid;
import com.example.pc.myapplication.carruselTools.CarruselPagerAdapter;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;

public class AtraccionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private Toolbar toolbar;
    private ReceiverOnAtraccion onAtraccion;
    private ReceiverOnAtraccionImgs onAtraccionImgs;
    private ReceiverOnAtraccionVid onAtraccionVid;

    public int PAGES = 1;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public final static int LOOPS = 1000;
    public int FIRST_PAGE = PAGES * LOOPS / 2;

    public CarruselPagerAdapter adapter;
    public ViewPager pager;
    private Atraccion atraccion;
    private GoogleMap map;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atraccion);
        toolbar = (Toolbar) findViewById(R.id.include3);
        toolbar.setTitle(getString(R.string.ciudades));
        setSupportActionBar(toolbar);

        view = findViewById(R.id.vertScrollView);
        String _id = getIntent().getStringExtra(Consts._ID);

        adapter = new CarruselPagerAdapter(this, this.getSupportFragmentManager());

        onAtraccion = new ReceiverOnAtraccion(this, view);
        onAtraccionImgs = new ReceiverOnAtraccionImgs(this, view, adapter);
        onAtraccionVid = new ReceiverOnAtraccionVid(this);

        String url = Consts.SERVER_URL + Consts.ATRACC + "/" + _id;

        InternetClient client = new InfoClient(getApplicationContext(), view,
                Consts.GET_ATR, url, null, Consts.GET, null, true);
        client.runInBackground();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);



        ///////////////////////////////////
        /*

        LinearLayout linlaIMG = (LinearLayout) findViewById(R.id.linlaIMG);
        LinearLayout linlaVIDEO = (LinearLayout) findViewById(R.id.linlaVIDEO);
        LinearLayout linlaAUDIO = (LinearLayout) findViewById(R.id.linlaAUDIO);

        List<LinearLayout> listLinla = new ArrayList<>(3);
        listLinla.add(linlaAUDIO);
        listLinla.add(linlaIMG);
        listLinla.add(linlaVIDEO);

        // SET THE IMAGEVIEW DIMENSIONS
        int dimens = 100;
        float density = getResources().getDisplayMetrics().density;
        int finalDimens = (int)(dimens * density);
        // SET THE MARGIN
        int dimensMargin = 4;
        float densityMargin = getResources().getDisplayMetrics().density;
        int finalDimensMargin = (int) (dimensMargin * densityMargin);


        for (int i = 0; i < list.size(); i++) {
            ImageView imgUsers = list.get(i);
            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgUsers.setLayoutParams(imgvwDimens);

            // SET SCALETYPE
            imgUsers.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);

            // ADD THE NEW IMAGEVIEW WITH THE PROFILE PICTURE LOADED TO THE LINEARLAYOUT
            linlaIMG.addView(imgUsers, imgvwMargin);

            imgUsers.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    System.out.println(v.getId());
                }
            });
        }

        for (int i = 0; i < listA.size(); i++) {
            ImageView imgUsers = listA.get(i);
            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgUsers.setLayoutParams(imgvwDimens);

            // SET SCALETYPE
            imgUsers.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);

            // ADD THE NEW IMAGEVIEW WITH THE PROFILE PICTURE LOADED TO THE LINEARLAYOUT
            linlaAUDIO.addView(imgUsers, imgvwMargin);

            imgUsers.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    System.out.println(v.getId());
                }
            });
        }

        for (int i = 0; i < listV.size(); i++) {
            ImageView imgUsers = listV.get(i);
            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgUsers.setLayoutParams(imgvwDimens);

            // SET SCALETYPE
            imgUsers.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);

            // ADD THE NEW IMAGEVIEW WITH THE PROFILE PICTURE LOADED TO THE LINEARLAYOUT
            linlaVIDEO.addView(imgUsers, imgvwMargin);

            imgUsers.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    System.out.println(v.getId());
                }
            });
        }*/

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        if (atraccion != null) {
           setMapContent();
        }
    }

    private void setMapContent() {
        LatLng latLng = new LatLng(atraccion.latitud, atraccion.longitud);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        map.addMarker(new MarkerOptions().position(latLng).flat(true));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtraccion,
                new IntentFilter(Consts.GET_ATR));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtraccionImgs,
                new IntentFilter(Consts.GET_ATR_IMG_S));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtraccionVid,
                new IntentFilter(Consts.GET_ATR_VID));

    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccion);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccionImgs);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccionVid);

    }

    public void attachAtraccion(Atraccion atraccion) {
        this.atraccion = atraccion;
        if (map != null) {
            setMapContent();
        }

        PAGES = atraccion.fotosPath.size();
        //adapter.notifyDataSetChanged();

        TextView atrName = (TextView) findViewById(R.id.nombreAtr);
        atrName.setText(atraccion.nombre);

        TextView atrInfo = (TextView) findViewById(R.id.infoText);
        atrInfo.setText(atraccion.descripcion);

        ImageView video = (ImageView) findViewById(R.id.videoIMG);
        video.setOnClickListener(this);
    }

    public Atraccion getAtraccion() {
        return  atraccion;
    }

    @Override
    public void onClick(View v) {
        String file = getCacheDir().getAbsolutePath() + "/" + atraccion._id + Consts.EXT;
        File videoFile = new File( file);

        if (!videoFile.exists()) {
            String url = Consts.SERVER_URL + Consts.ATRACC + "/" + atraccion._id + Consts.VIDEO;

            InternetClient client = new VideoClient(getApplicationContext(), view,
                    Consts.GET_ATR_VID, url, null, Consts.GET, null, true, atraccion._id);
            client.runInBackground();
        } else {
            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setDataSource(file);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
