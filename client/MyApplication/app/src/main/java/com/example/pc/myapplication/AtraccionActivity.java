package com.example.pc.myapplication;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccion;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionImgs;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAtraccionPlano;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAudCheck;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnVidCheck;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnVidThumbnail;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.carruselTools.CarruselPagerAdapter;
import com.example.pc.myapplication.ciudadesTools.Atraccion;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.mapTools.MapInfoWindowAdapter;
import com.example.pc.myapplication.mediaPlayers.AudioPlayer;
import com.example.pc.myapplication.mediaPlayers.PlayPauseListener;
import com.example.pc.myapplication.mediaPlayers.VideoPlayer;
import com.example.pc.myapplication.singletons.ImagesSingleton;
import com.example.pc.myapplication.singletons.PosVideoSingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class AtraccionActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                        View.OnClickListener,
                                                        GoogleMap.OnMapClickListener,
                                                        DialogInterface.OnCancelListener,
                                                        View.OnTouchListener,
                                                        MediaPlayer.OnPreparedListener {

    private static final long DOUBLE_PRESS_INTERVAL = 250;//double tap

    private Toolbar toolbar;
    private ReceiverOnAtraccion onAtraccion;
    private ReceiverOnAtraccionImgs onAtraccionImgs;
    private ReceiverOnVidThumbnail onAtrVidThumb;
    private ReceiverOnAtraccionPlano onAtrPlano;
    private ReceiverOnAudCheck onAudCheck;
    private ReceiverOnVidCheck onVidCheck;

    private VideoPlayer videoPlayerView;
    private MediaPlayer audioPlayer;
    private ProgressDialog progressDialog;
    private MediaController videoController;
    private MediaController audioController;

    private StringBuilder heightVideo;
    private StringBuilder weigthVideo;

    public int PAGES = 1;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atraccion);
        toolbar = (Toolbar) findViewById(R.id.include3);
        toolbar.setTitle(R.string.atraccion);
        setSupportActionBar(toolbar);

        ImagesSingleton.getInstance().clear();

        view = findViewById(R.id.masterAtraccion);

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

        String urlConst = ((TripTP)getApplication()).getUrl() + Consts.ATRACC + "/" + _id;

        onAtraccion = new ReceiverOnAtraccion(this, view);
        onAtraccionImgs = new ReceiverOnAtraccionImgs(this, view, adapter, pager);
        onAtrVidThumb = new ReceiverOnVidThumbnail(view,heightVideo,weigthVideo, this);
        onAtrPlano = new ReceiverOnAtraccionPlano(view);
        onVidCheck = new ReceiverOnVidCheck(view, urlConst, this);
        onAudCheck = new ReceiverOnAudCheck(view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        videoPlayerView = (VideoPlayer) findViewById(R.id.miniVideoView);
        videoPlayerView.setPlayPauseListener(new PlayPauseListener() {
            @Override
            public void onPlay() {
                stopAudio();
            }

            @Override
            public void onPause() {}
        });
        videoBtn = (ImageView) findViewById(R.id.plBtn);

        audioPlayer = new MediaPlayer();

        if (videoController == null) {
            videoController = new MediaController(this);
        }

        if (audioController == null) {
            audioController = new MediaController(this);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);

        InternetClient client = new InfoClient(getApplicationContext(), view,
                Consts.GET_ATR, urlConst, null, Consts.GET, null, true);
        client.runInBackground();

        String urlVideo = urlConst + Consts.VIDEO;
        InternetClient clientVid = new InfoClient(getApplicationContext(), view,
                Consts.GET_CHECK_VID, urlVideo, null, Consts.GET, null, false);
        clientVid.runInBackground();

        try {
            String idioma = URLEncoder.encode(Locale.getDefault().getDisplayLanguage().toLowerCase(), "utf-8");
            String urlAudio = urlConst + Consts.AUDIO + "?" + Consts.IDIOMA + "=" + idioma;
            InternetClient clientAud = new InfoClient(getApplicationContext(), view,
                    Consts.GET_CHECK_AUD, urlAudio, null, Consts.GET, null, true);
            clientAud.runInBackground();
        } catch (UnsupportedEncodingException e) {

        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setInfoWindowAdapter(new MapInfoWindowAdapter(getLayoutInflater()));
        if (atraccion != null) {
           setMapContent();
        }
        map.setOnMapClickListener(this);
    }

    private void setMapContent() {
        LatLng latLng = new LatLng(atraccion.latitud, atraccion.longitud);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        map.addMarker(new MarkerOptions()
                .title(atraccion.nombre)
                .snippet(atraccion.descripcion)
                .position(latLng)
                .flat(true))
                .setTag(0);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtraccion,
                new IntentFilter(Consts.GET_ATR));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onVidCheck,
                new IntentFilter(Consts.GET_CHECK_VID));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAudCheck,
                new IntentFilter(Consts.GET_CHECK_AUD));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtraccionImgs,
                new IntentFilter(Consts.GET_ATR_IMG_S));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtrVidThumb,
                new IntentFilter(Consts.GET_VID_THU));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onAtrPlano,
                new IntentFilter(Consts.GET_ATR_PLANO));
        super.onStart();

    }

    public void onStop() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccion);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onVidCheck);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAudCheck);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtraccionImgs);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtrVidThumb);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onAtrPlano);
        super.onStop();

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

        if (videoPlayerView != null && PosVideoSingleton.getInstance().isPlaying()) {
            videoPlayerView.seekTo(PosVideoSingleton.getInstance().getPosition());
            this.onClick(videoPlayerView);

        }

        toFullScreen = false;

        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (PosVideoSingleton.getInstance().isPlaying()) {
            PosVideoSingleton.getInstance().setHaveToKeepPlaying(true);
        } else {
            PosVideoSingleton.getInstance().setHaveToKeepPlaying(false);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        ImagesSingleton.getInstance().clear();
        PosVideoSingleton.getInstance().clear();
        stopAudio();
        audioPlayer.release();
        super.onBackPressed();
    }

    private void stopAudio() {
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.pause();
        }
        if (audioController != null && audioController.isShowing())
            audioController.hide();
    }

    private void stopVideo() {
        if (videoPlayerView != null && videoPlayerView.isPlaying()) {
            videoPlayerView.pause();
        }
        if (videoController != null && videoController.isShowing())
            videoController.hide();
    }

    public void attachAtraccion(final Atraccion atraccion) {
        this.atraccion = atraccion;
        if (map != null) {
            setMapContent();
        }

        PAGES = atraccion.fotosPath.size();
        adapter.notifyDataSetChanged();

        pager.setAdapter(adapter);
        pager.setPageTransformer(false, adapter);

        FIRST_PAGE = PAGES * LOOPS / 2;
        pager.setCurrentItem(FIRST_PAGE);

        pager.setOffscreenPageLimit(3);

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
        audioName.setText(Html.fromHtml( "<a href=>" + atraccion.nombre + " 01" + "</a> "));

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

        videoPlayerView.setOnTouchListener(this);
        view.setVisibility(View.VISIBLE);

        ScrollView scroll = (ScrollView) view.findViewById(R.id.vertScrollView);
        scroll.scrollTo(0,0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(!toFullScreen)
            PosVideoSingleton.getInstance().setPosition(videoPlayerView.getCurrentPosition());

        if ( _id != null && !_id.isEmpty())
            savedInstanceState.putString(Consts._ID, _id);
        if (PosVideoSingleton.getInstance().isPlaying()) {
            videoPlayerView.pause();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        _id = savedInstanceState.getString(Consts._ID);
        if (PosVideoSingleton.getInstance().isPlaying()) {
            videoPlayerView.seekTo(PosVideoSingleton.getInstance().getPosition());
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

        String url = ((TripTP)getApplication()).getUrl() + Consts.ATRACC + "/" + _id + Consts.VIDEO;

        try {
            videoPlayerView.setMediaController(videoController);
            if(videoController != null){
                videoController.setMediaPlayer(videoPlayerView);
                videoController.setAnchorView(videoPlayerView);
            }
            videoPlayerView.setVideoURI(Uri.parse(url));

        } catch (Exception e) {
            Toast.makeText(this,"No se pudo inializar video", Toast.LENGTH_LONG).show();
        }

        PosVideoSingleton.getInstance().setHaveToKeepPlaying(true);
        videoPlayerView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (!isProgressCanceled) {
                    progressDialog.dismiss();
                    videoPlayerView.seekTo(PosVideoSingleton.getInstance().getPosition());
                    if (PosVideoSingleton.getInstance().haveToKeepPlaying())
                        videoPlayerView.start();
                    else
                        videoPlayerView.pause();

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
        mapAct.putExtra(Consts.NOMBRE, atraccion.nombre);
        mapAct.putExtra(Consts.DESCRIPCION, atraccion.descripcion);
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
                PosVideoSingleton.getInstance().setPosition(videoPlayerView.getCurrentPosition());
                PosVideoSingleton.getInstance().setHaveToKeepPlaying(videoPlayerView.isPlaying());
                videoPlayerView.pause();
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

    public void audioClick(View view) {
        if (!audioPlayer.isPlaying()) {
            findViewById(R.id.controllerHolder).setVisibility(View.VISIBLE);
            stopVideo();
            try {
                audioPlayer.reset();
                String idioma = URLEncoder.encode(Locale.getDefault().getDisplayLanguage().toLowerCase(), "utf-8");
                String url = ((TripTP) getApplication()).getUrl() + Consts.ATRACC + "/" + atraccion._id + Consts.AUDIO + "?" + Consts.IDIOMA + "=" + idioma;
                audioPlayer.setDataSource(url);
                audioPlayer.prepareAsync();

                audioPlayer.setOnPreparedListener(this);

            } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        audioController.setMediaPlayer(new AudioPlayer(audioPlayer,audioController,videoPlayerView, videoController));
        Handler handler = new Handler();
        audioController.setAnchorView(findViewById(R.id.controllerHolder));
        handler.post(new Runnable() {

            public void run() {
                audioController.setEnabled(true);
                audioController.show(0);
            }
        });
        audioPlayer.start();
    }

    public void showAudioController(View view) {
        if (audioController != null) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                public void run() {
                    audioController.setEnabled(true);
                    audioController.show(0);
                }
            });
        }
    }
}
