package com.example.pc.myapplication;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.VideoClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccion;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionImgs;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionVid;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnVidThumbnail;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.carruselTools.CarruselPagerAdapter;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.commonfunctions.PathJSONParser;
import com.example.pc.myapplication.singletons.ImagesSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AtraccionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener {

    private Toolbar toolbar;
    private ReceiverOnAtraccion onAtraccion;
    private ReceiverOnAtraccionImgs onAtraccionImgs;
    private ReceiverOnAtraccionVid onAtraccionVid;
    private ReceiverOnVidThumbnail onAtrVidThumb;

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

        ImageView img = (ImageView) findViewById(R.id.videoIMG) ;
        pager = (ViewPager) findViewById(R.id.myviewpager);

        onAtraccion = new ReceiverOnAtraccion(this, view);
        onAtraccionImgs = new ReceiverOnAtraccionImgs(this, view, adapter, pager);
        onAtraccionVid = new ReceiverOnAtraccionVid(this);
        onAtrVidThumb = new ReceiverOnVidThumbnail(img,this);

        String url = ((TripTP)getApplication()).getUrl() + Consts.ATRACC + "/" + _id;

        InternetClient client = new InfoClient(getApplicationContext(), view,
                Consts.GET_ATR, url, null, Consts.GET, null, true);
        client.runInBackground();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        if (atraccion != null) {
           setMapContent();
        }
        map.setOnMapClickListener(this);
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
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtrVidThumb,
                new IntentFilter(Consts.GET_VID_THU));

    }

    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccion);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccionImgs);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccionVid);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtrVidThumb);

    }

    @Override
    public void onResume() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(ImagesSingleton.getInstance().getCurrentPosition()*LOOPS / 2);
            }
        });

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        ImagesSingleton.getInstance().clear();
        super.onBackPressed();
    }

    public void attachAtraccion(Atraccion atraccion) {
        this.atraccion = atraccion;
        if (map != null) {
            setMapContent();
        }
        Log.i("IMGConn", "Set pagges");
        PAGES = atraccion.fotosPath.size();
        Log.i("IMGConn", "notify pages");
        adapter.notifyDataSetChanged();

        pager.setAdapter(adapter);
        pager.setPageTransformer(false, adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        FIRST_PAGE = PAGES * LOOPS / 2;
        pager.setCurrentItem(FIRST_PAGE);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        pager.setPageMargin(-200);

        TextView atrName = (TextView) findViewById(R.id.nombreAtr);
        atrName.setText(atraccion.nombre);

        TextView atrInfo = (TextView) findViewById(R.id.infoText);
        atrInfo.setText(atraccion.descripcion);

        TextView atrClassf = (TextView) findViewById(R.id.clasifica);
        atrClassf.setText( fillFields(atrClassf.getText().toString(), atraccion.clasificacion));

        TextView atrCosto = (TextView) findViewById(R.id.costo);
        atrCosto.setText( fillFields(atrCosto.getText().toString(), "$" + String.valueOf(atraccion.costo)));

        TextView atrAper = (TextView) findViewById(R.id.horaAp);
        atrAper.setText( fillFields (atrAper.getText().toString(), atraccion.horaApert));

        TextView atrCierre = (TextView) findViewById(R.id.horacierre);
        atrCierre.setText(fillFields (atrCierre.getText().toString(), atraccion.horaCierre));

        TextView atrDuracion = (TextView) findViewById(R.id.duracion);
        atrDuracion.setText(fillFields(atrDuracion.getText().toString(), String.valueOf(atraccion.duracion)));

        TextView atrVoteCount = (TextView) findViewById(R.id.votesCount);
        atrVoteCount.setText(" "+  atraccion.cantVotos);

        TextView audioName = (TextView) findViewById(R.id.audio01);
        audioName.setText(Html.fromHtml( "<a href=\"google.com\">" + atraccion.nombre + " 01" + "</a> "));
        audioName.setMovementMethod(LinkMovementMethod.getInstance());

        LinearLayout ln = (LinearLayout) findViewById(R.id.ratingIMG);
        ImageView stars = (ImageView) ln.findViewById(R.id.star);
        ViewGroup.LayoutParams params = stars.getLayoutParams();
        ln.removeViewAt(1); //remuevo primer estrella solo la puse para setear parametros

        int entero = (int) atraccion.rating;

        for (int i = 0; i < entero; i++) {
            ImageView imgNew = new ImageView(this);
            imgNew.setLayoutParams(params);
            imgNew.setImageResource(R.drawable.star);
            ln.addView(imgNew,1); //+1 textview
        }

        int medio = 0;

        if (atraccion.rating - entero != 0 ) {
            ImageView imgNew = new ImageView(this);
            imgNew.setLayoutParams(params);
            imgNew.setImageResource(R.drawable.star_half);
            ln.addView(imgNew,entero + 1); //+1 textview
            medio = 1;
        }
        //+1 textview
        for(int i = entero + medio; i < Consts.CANT_STARS; i++) {
            ImageView imgNew = new ImageView(this);
            imgNew.setLayoutParams(params);
            imgNew.setImageResource(R.drawable.star_outline);
            ln.addView(imgNew,entero + medio + 1); //+1 textview
        }

        ImageView video = (ImageView) findViewById(R.id.plBtn);
        video.setOnClickListener(this);
    }

    public Spannable fillFields(String title, String add) {
        Spannable spannable = new SpannableString(title + " " + add);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    @Override
    public void onClick(View v) {
        String file = getCacheDir().getAbsolutePath() + "/" + atraccion._id + Consts.EXT;
        File videoFile = new File( file);

        if (!videoFile.exists()) {
            String url = ((TripTP)getApplication()).getUrl() + Consts.ATRACC + "/" + atraccion._id + Consts.VIDEO;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("org.videolan.vlc"); // Use org.videolan.vlc for nightly builds
            intent.setDataAndType(Uri.parse(url), "application/mp4");
            startActivity(intent);

        /*   InternetClient client = new VideoClient(getApplicationContext(), view,
                    Consts.GET_ATR_VID, url, null, Consts.GET, null, true, atraccion._id);
            client.runInBackground();

            ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Cargando video");
            progress.setMessage("Por favor espere...");
            progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
            progress.show();

            onAtraccionVid.setProgress(progress);*/

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

    @Override
    public void onMapClick(LatLng latLng) {
        Intent mapAct = new Intent(this, MapActivity.class);
        mapAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapAct.putExtra(Consts.LATITUD, atraccion.latitud);
        mapAct.putExtra(Consts.LONGITUD, atraccion.longitud);
        this.startActivity(mapAct);
    }



}
