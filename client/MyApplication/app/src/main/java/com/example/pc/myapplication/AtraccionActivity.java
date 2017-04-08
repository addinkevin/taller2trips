package com.example.pc.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccion;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionImgs;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionVid;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnVidThumbnail;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.carruselTools.CarruselPagerAdapter;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.singletons.ImagesSingleton;
import com.example.pc.myapplication.singletons.PosVideoSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AtraccionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener, DialogInterface.OnCancelListener, View.OnTouchListener {

    private static final long DOUBLE_PRESS_INTERVAL = 250;
    private Toolbar toolbar;
    private ReceiverOnAtraccion onAtraccion;
    private ReceiverOnAtraccionImgs onAtraccionImgs;
    private ReceiverOnAtraccionVid onAtraccionVid;
    private ReceiverOnVidThumbnail onAtrVidThumb;

    private VideoView videoView;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

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
    private ImageView videoThumb;
    private ImageView videoBtn;
    private boolean isProgressCanceled;
    private long lastPressTime;
    private String _id;
    private boolean toFullScreen;
    private StringBuilder heightVideo;
    private StringBuilder weigthVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atraccion);
        toolbar = (Toolbar) findViewById(R.id.include3);
        toolbar.setTitle(getString(R.string.ciudades));
        setSupportActionBar(toolbar);

        ImagesSingleton.getInstance().clear();

        view = findViewById(R.id.masterAtraccion);
        view.setVisibility(View.INVISIBLE);
        if ( _id == null || _id.isEmpty()) {
            _id = getIntent().getStringExtra(Consts._ID);
        }

        isProgressCanceled = false;
        toFullScreen = false;
        weigthVideo = new StringBuilder();
        heightVideo = new StringBuilder();

        adapter = new CarruselPagerAdapter(this, this.getSupportFragmentManager());

        videoThumb = (ImageView) findViewById(R.id.videoIMG) ;
        pager = (ViewPager) findViewById(R.id.myviewpager);

        onAtraccion = new ReceiverOnAtraccion(this, view);
        onAtraccionImgs = new ReceiverOnAtraccionImgs(this, view, adapter, pager);
        onAtraccionVid = new ReceiverOnAtraccionVid(this);
        onAtrVidThumb = new ReceiverOnVidThumbnail(videoThumb,heightVideo,weigthVideo, this);

        String url = ((TripTP)getApplication()).getUrl() + Consts.ATRACC + "/" + _id;

        InternetClient client = new InfoClient(getApplicationContext(), view,
                Consts.GET_ATR, url, null, Consts.GET, null, true);
        client.runInBackground();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        videoView = (VideoView) findViewById(R.id.miniVideoView);
        videoBtn = (ImageView) findViewById(R.id.plBtn);

        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("no se que poner");
        progressDialog.setMessage("cargando...");
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);

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
        if (ImagesSingleton.getInstance().getCurrentPosition() != 0) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(ImagesSingleton.getInstance().getCurrentPosition() * LOOPS / 2);
                }
            });
        }

        if (videoView != null && PosVideoSingleton.getInstance().isPlaying()) {
            videoView.seekTo(PosVideoSingleton.getInstance().getPosition());
            this.onClick(videoView);

        }

        toFullScreen = false;

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        ImagesSingleton.getInstance().clear();
        PosVideoSingleton.getInstance().clear();
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

        videoBtn.setOnClickListener(this);

        videoView.setOnTouchListener(this);
        view.setVisibility(View.VISIBLE);
        ScrollView scroll = (ScrollView) view.findViewById(R.id.vertScrollView);
        scroll.scrollTo(0,0);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(!toFullScreen)
            PosVideoSingleton.getInstance().setPosition(videoView.getCurrentPosition());

        if ( _id != null && !_id.isEmpty())
            savedInstanceState.putString(Consts._ID, _id);
        if (PosVideoSingleton.getInstance().isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        _id = savedInstanceState.getString(Consts._ID);
        if (PosVideoSingleton.getInstance().isPlaying()) {
            videoView.seekTo(PosVideoSingleton.getInstance().getPosition());
        }
    }

    public Spannable fillFields(String title, String add) {
        Spannable spannable = new SpannableString(title + " " + add);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    @Override
    public void onClick(View v) {
        isProgressCanceled = false;
        progressDialog.show();
        videoBtn.setVisibility(View.INVISIBLE);
        videoThumb.setVisibility(View.INVISIBLE);
        videoBtn.setOnClickListener(null);

        final String url = ((TripTP)getApplication()).getUrl() + Consts.ATRACC + "/" + _id + Consts.VIDEO;

        try {
            videoView.setMediaController(mediaControls);
            if(mediaControls != null){
                mediaControls.setMediaPlayer(videoView);
            }
            videoView.setVideoURI(Uri.parse(url));

        } catch (Exception e) {
            Toast.makeText(this,"No se pudo inializar video", Toast.LENGTH_LONG).show();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (!isProgressCanceled) {
                    progressDialog.dismiss();
                    videoView.seekTo(PosVideoSingleton.getInstance().getPosition());

                    PosVideoSingleton.getInstance().setPlaying(true);
                    videoView.start();

                }
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent mapAct = new Intent(this, MapActivity.class);
        mapAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapAct.putExtra(Consts.LATITUD, atraccion.latitud);
        mapAct.putExtra(Consts.LONGITUD, atraccion.longitud);
        this.startActivity(mapAct);
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        isProgressCanceled = true;
        videoBtn.setVisibility(View.VISIBLE);
        videoThumb.setVisibility(View.VISIBLE);
        videoBtn.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            long pressTime = System.currentTimeMillis();

            boolean mHasDoubleClicked;
            if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
                String url = ((TripTP) getApplication()).getUrl() + Consts.ATRACC + "/" + atraccion._id + Consts.VIDEO;
                Intent videoAct = new Intent(this, VideoActivity.class);
                videoAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                videoAct.putExtra(Consts.URL, url);
                videoAct.putExtra(Consts.IMG_H, heightVideo.toString());
                videoAct.putExtra(Consts.IMG_W, weigthVideo.toString());
                PosVideoSingleton.getInstance().setPosition(videoView.getCurrentPosition());
                videoView.pause();
                toFullScreen = true;
                this.startActivity(videoAct);
                mHasDoubleClicked = true;
            } else {
                mHasDoubleClicked = false;
            }

            lastPressTime = pressTime;
            return mHasDoubleClicked;
        } else {
            return false;
        }
    }
}
