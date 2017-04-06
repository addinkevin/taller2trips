package com.example.pc.myapplication;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.pc.myapplication.commonfunctions.Consts;

public class VideoActivity extends AppCompatActivity {
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

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


        myVideoView = (VideoView) findViewById(R.id.videoView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("no se que poner");
        progressDialog.setMessage("cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = getIntent().getStringExtra(Consts._ID);

        try {
            myVideoView.setMediaController(mediaControls);
            myVideoView.setVideoURI(Uri.parse(url));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    myVideoView.pause();
                }
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }

}



