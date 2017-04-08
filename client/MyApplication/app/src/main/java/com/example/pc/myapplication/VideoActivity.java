package com.example.pc.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.pc.myapplication.commonfunctions.Consts;
import com.example.pc.myapplication.singletons.PosVideoSingleton;

public class VideoActivity extends Activity implements MediaPlayer.OnPreparedListener {
    private VideoView videoView;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private int defaultOrientation;
    private int videoH;
    private int videoW;

    private void hideClockBateryBar(){
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        hideClockBateryBar();

        if (mediaControls == null) {
            mediaControls = new MediaController(this);

        }

        defaultOrientation = getRequestedOrientation();


        videoView = (VideoView) findViewById(R.id.videoView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("no se que poner");
        progressDialog.setMessage("cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = getIntent().getStringExtra(Consts.URL);
        try {
            videoH = Integer.parseInt(getIntent().getStringExtra(Consts.IMG_H));
            videoW = Integer.parseInt(getIntent().getStringExtra(Consts.IMG_W));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            videoH = 0;
            videoW = 0;
        }

        try {
            videoView.setMediaController(mediaControls);
            videoView.setVideoURI(Uri.parse(url));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoView.requestFocus();
        videoView.setOnPreparedListener(this);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        PosVideoSingleton.getInstance().setPosition(videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        videoView.seekTo(PosVideoSingleton.getInstance().getPosition());
    }

    @Override
    public void onBackPressed() {
        PosVideoSingleton.getInstance().setPosition(videoView.getCurrentPosition());
        videoView.pause();
        setRequestedOrientation(defaultOrientation);
        super.onBackPressed();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        progressDialog.dismiss();
        int vvHeight;
        int vvWidth;
        if (videoH == 0 || videoW == 0) {
            vvHeight = videoView.getHeight();
            vvWidth = videoView.getWidth();
        } else {
            vvHeight = videoH;
            vvWidth = videoW;
        }
        if (vvWidth >= vvHeight) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        videoView.seekTo(PosVideoSingleton.getInstance().getPosition());
        videoView.start();

    }
}



