package com.example.pc.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.myapplication.InternetTools.InfoClient;
import com.example.pc.myapplication.InternetTools.InternetClient;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnAudCheckPI;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnPuntoInteres;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnPuntoInteresImgs;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnVidCheckPI;
import com.example.pc.myapplication.InternetTools.receivers.ReceiverOnVidThumbnailPI;
import com.example.pc.myapplication.application.TripTP;
import com.example.pc.myapplication.carruselTools.CarruselPagerPIAdapter;
import com.example.pc.myapplication.ciudadesTools.PuntoInteres;
import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.mediaPlayers.AudioPlayer;
import com.example.pc.myapplication.mediaPlayers.PlayPauseListener;
import com.example.pc.myapplication.mediaPlayers.VideoPlayer;
import com.example.pc.myapplication.singletons.ImagesSingletonPI;
import com.example.pc.myapplication.singletons.NetClientsSingleton;
import com.example.pc.myapplication.singletons.PosVideoSingleton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;

public class PuntoInteresTab extends Fragment implements View.OnClickListener, MediaPlayer.OnPreparedListener,  View.OnTouchListener, DialogInterface.OnCancelListener {

    public int PAGES = 1;
    public final static int LOOPS = 1000;
    public int FIRST_PAGE = PAGES * LOOPS / 2;
    private static final long DOUBLE_PRESS_INTERVAL = 250;//double tap

    private View view;
    private String _id;
    private boolean isProgressCanceled;
    private boolean toFullScreen;
    private StringBuilder weigthVideo;
    private StringBuilder heightVideo;
    private CarruselPagerPIAdapter adapter;
    private ImageView videoThumb;
    private ViewPager pager;
    private ReceiverOnPuntoInteres onPI;
    private ReceiverOnPuntoInteresImgs onPIImgs;
    private ReceiverOnVidThumbnailPI onPIVidThumb;
    private ReceiverOnVidCheckPI onVidCheck;
    private ReceiverOnAudCheckPI onAudCheck;
    private VideoPlayer videoPlayerView;
    private ImageView videoBtn;
    private MediaPlayer audioPlayer;
    private MediaController videoController;
    private MediaController audioController;
    private ProgressDialog progressDialog;
    private View fragView;

    private FragmentActivity activity;
    private PuntoInteres puntoInteres;
    private long lastPressTime;

    private ComentariosPITab comentariosTab;
    private TripTP tripTP;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.punto_interes_tab, container, false);

        ImagesSingletonPI.getInstance().clear();

        view = fragView.findViewById(R.id.masterAtraccion);

        activity = getActivity();

        if ( _id == null || _id.isEmpty()) {
            _id = activity.getIntent().getStringExtra(Consts._ID);
        }

        disponible = true;

        fragView.findViewById(R.id.nextAtr).setVisibility(View.GONE);
        fragView.findViewById(R.id.prevAtr).setVisibility(View.GONE);

        isProgressCanceled = false;
        toFullScreen = false;
        weigthVideo = new StringBuilder();
        heightVideo = new StringBuilder();

        adapter = new CarruselPagerPIAdapter(this, activity.getSupportFragmentManager());

        videoThumb = (ImageView) fragView.findViewById(R.id.videoIMG) ;
        pager = (ViewPager) fragView.findViewById(R.id.myviewpager);

        tripTP = (TripTP)activity.getApplication();

        String urlConst = tripTP.getUrl() + Consts.PI + "/" + _id;

        onPI = new ReceiverOnPuntoInteres(this, view);
        onPIImgs = new ReceiverOnPuntoInteresImgs(view, adapter, pager);
        onPIVidThumb = new ReceiverOnVidThumbnailPI(view,heightVideo,weigthVideo);
        onVidCheck = new ReceiverOnVidCheckPI(view, urlConst);
        onAudCheck = new ReceiverOnAudCheckPI(view);

        videoPlayerView = (VideoPlayer) fragView.findViewById(R.id.miniVideoView);
        videoPlayerView.setPlayPauseListener(new PlayPauseListener() {
            @Override
            public void onPlay() {
                stopAudio();
            }

            @Override
            public void onPause() {}
        });
        videoBtn = (ImageView) fragView.findViewById(R.id.plBtn);

        audioPlayer = new MediaPlayer();

        if (videoController == null) {
            videoController = new MediaController(activity);
        }

        if (audioController == null) {
            audioController = new MediaController(activity);
        }

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);

        Map<String,String> headerIdioma = Consts.getHeaderIdiomaCategoria();

        InternetClient client = new InfoClient(activity.getApplicationContext(),
                Consts.GET_PI, urlConst, headerIdioma, Consts.GET, null, true);
        NetClientsSingleton.getInstance().add(client.createTask());
        client.runInBackground();

        String urlVideo = urlConst + Consts.VIDEO;
        InternetClient clientVid = new InfoClient(activity.getApplicationContext(),
                Consts.GET_CHECK_VID_PI, urlVideo, null, Consts.GET, null, false);
        NetClientsSingleton.getInstance().add(clientVid.createTask());
        clientVid.runInBackground();

        try {
            String idioma = URLEncoder.encode(Locale.getDefault().getLanguage().toLowerCase(), "utf-8");
            String urlAudio = urlConst + Consts.AUDIO + "?" + Consts.IDIOMA + "=" + idioma;
            InternetClient clientAud = new InfoClient(activity.getApplicationContext(),
                    Consts.GET_CHECK_AUD_PI, urlAudio, null, Consts.GET, null, true);
            NetClientsSingleton.getInstance().add(clientAud.createTask());
            clientAud.runInBackground();

        } catch (UnsupportedEncodingException e) {

        }

        return fragView;
    }

    public void onStart() {
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onPI,
                new IntentFilter(Consts.GET_PI));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onVidCheck,
                new IntentFilter(Consts.GET_CHECK_VID_PI));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onAudCheck,
                new IntentFilter(Consts.GET_CHECK_AUD_PI));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onPIImgs,
                new IntentFilter(Consts.GET_ATR_IMG_S_PI));
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).registerReceiver(onPIVidThumb,
                new IntentFilter(Consts.GET_VID_THU_PI));
        super.onStart();

    }

    public void onStop() {
        unregister();
        super.onStop();

    }

    public void unregister() {
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onPI);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onVidCheck);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onAudCheck);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onPIImgs);
        LocalBroadcastManager.getInstance(activity.getApplicationContext()).unregisterReceiver(onPIVidThumb);
    }

    @Override
    public void onResume() {
        if (ImagesSingletonPI.getInstance().getCurrentPosition() != 0) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(ImagesSingletonPI.getInstance().getCurrentPosition() * LOOPS / 2);
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

    public void onBackPressed() {
        ImagesSingletonPI.getInstance().clear();
        PosVideoSingleton.getInstance().clear();
        stopAudio();
        audioPlayer.release();
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

    public void attachPuntoInteres(final PuntoInteres puntoInteres) {
        this.puntoInteres = puntoInteres;
        comentariosTab.attachPuntoInteres(puntoInteres);

        PAGES = puntoInteres.fotosPath.size();
        adapter.notifyDataSetChanged();

        pager.setAdapter(adapter);
        pager.setPageTransformer(false, adapter);

        FIRST_PAGE = PAGES * LOOPS / 2;
        pager.setCurrentItem(FIRST_PAGE);

        pager.setOffscreenPageLimit(3);

        pager.setPageMargin(-200);

        TextView atrName = (TextView) fragView.findViewById(R.id.nombreAtr);
        atrName.setText(puntoInteres.nombre);

        TextView atrInfo = (TextView) fragView.findViewById(R.id.infoText);
        atrInfo.setText(puntoInteres.descripcion);

        TextView atrClassf = (TextView) fragView.findViewById(R.id.clasifica);
        atrClassf.setText( fillFields(atrClassf.getText().toString(), puntoInteres.clasificacion));

        TextView atrCosto = (TextView) fragView.findViewById(R.id.costo);
        atrCosto.setText( fillFields(atrCosto.getText().toString(), "$" + String.valueOf(puntoInteres.costo)));

        TextView atrMoneda = (TextView) fragView.findViewById(R.id.moneda);
        atrMoneda.setText( fillFields(atrMoneda.getText().toString(), puntoInteres.moneda));

        TextView atrAper = (TextView) fragView.findViewById(R.id.horaAp);
        atrAper.setText( fillFields (atrAper.getText().toString(), puntoInteres.horaApert));

        TextView atrCierre = (TextView) fragView.findViewById(R.id.horacierre);
        atrCierre.setText(fillFields (atrCierre.getText().toString(), puntoInteres.horaCierre));

        TextView atrDuracion = (TextView) fragView.findViewById(R.id.duracion);
        atrDuracion.setText(fillFields(atrDuracion.getText().toString(), String.valueOf(puntoInteres.duracion)));

        TextView atrVoteCount = (TextView) fragView.findViewById(R.id.votesCount);
        atrVoteCount.setText(" "+  puntoInteres.cantVotos);

        TextView audioName = (TextView) fragView.findViewById(R.id.audio01);
        audioName.setText(Html.fromHtml( "<a href=>" + puntoInteres.nombre + " 01" + "</a> "));

        RatingBar stars = (RatingBar) fragView.findViewById(R.id.ratingBar2);
        stars.setRating(puntoInteres.rating);

        videoBtn.setOnClickListener(this);

        videoPlayerView.setOnTouchListener(this);
        view.setVisibility(View.VISIBLE);

        ScrollView scroll = (ScrollView) view.findViewById(R.id.vertScrollView);
        scroll.scrollTo(0,0);
    }

    public TripTP getAplicacion() {
       return (TripTP) activity.getApplication();
    }

    public Context getAplicationContext() {
        return activity.getApplicationContext();
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

    public void onRestoreInstanceState(Bundle savedInstanceState) {
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

        String url = tripTP.getUrl() + Consts.PI + "/" + _id + Consts.VIDEO;

        try {
            videoPlayerView.setMediaController(videoController);
            if(videoController != null){
                videoController.setMediaPlayer(videoPlayerView);
                videoController.setAnchorView(videoPlayerView);
            }
            videoPlayerView.setVideoURI(Uri.parse(url));

        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(),"No se pudo inializar video", Toast.LENGTH_LONG).show();
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
                String url = tripTP.getUrl() + Consts.PI + "/" + puntoInteres._id + Consts.VIDEO;
                Intent videoAct = new Intent(activity, VideoActivity.class);
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

    public void audioClick() {
        if (!audioPlayer.isPlaying()) {
            fragView.findViewById(R.id.controllerHolder).setVisibility(View.VISIBLE);
            stopVideo();
            try {
                audioPlayer.reset();
                String idioma = URLEncoder.encode(Locale.getDefault().getLanguage().toLowerCase(), "utf-8");
                String url = tripTP.getUrl() + Consts.PI + "/" + puntoInteres._id + Consts.AUDIO + "?" + Consts.IDIOMA + "=" + idioma;
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
        audioController.setAnchorView(fragView.findViewById(R.id.controllerHolder));
        handler.post(new Runnable() {

            public void run() {
                audioController.setEnabled(true);
                audioController.show(0);
            }
        });
        audioPlayer.start();
    }

    public void showAudioController() {
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

    public void setComentariosTab(ComentariosPITab comentariosTab) {
        this.comentariosTab = comentariosTab;
    }
}
