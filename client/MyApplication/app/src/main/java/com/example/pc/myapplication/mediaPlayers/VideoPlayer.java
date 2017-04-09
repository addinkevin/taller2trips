package com.example.pc.myapplication.mediaPlayers;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.pc.myapplication.singletons.PosVideoSingleton;

public class VideoPlayer extends VideoView{

    private MediaPlayer audioPlayer;
    private MediaController audioController;
    private VideoView videoPlayerView;
    private MediaController videoController;
    private PlayPauseListener mListener;

    public VideoPlayer(Context context) {
        super(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }

    @Override
    public void start() {
        if (mListener != null) {
            mListener.onPlay();
        }
        PosVideoSingleton.getInstance().setPlaying(true);
        super.start();
    }

    @Override
    public void pause() {
        if (mListener != null) {
            mListener.onPause();
        }
        PosVideoSingleton.getInstance().setPlaying(false);
        super.pause();
    }
}

