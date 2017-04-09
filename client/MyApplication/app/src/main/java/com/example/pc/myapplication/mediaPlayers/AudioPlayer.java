package com.example.pc.myapplication.mediaPlayers;

import android.media.MediaPlayer;
import android.widget.MediaController;
import android.widget.VideoView;

public class AudioPlayer implements MediaController.MediaPlayerControl{

    private MediaPlayer audioPlayer;
    private MediaController audioController;
    private VideoView videoPlayerView;
    private MediaController videoController;

    private void stopVideo() {
        if (videoPlayerView != null && videoPlayerView.isPlaying()) {
            videoPlayerView.pause();
        }
        if (videoController != null && videoController.isShowing())
            videoController.hide();
    }


    public AudioPlayer(MediaPlayer audioPlayer, MediaController audioController, VideoView videoPlayerView, MediaController videoController) {
        this.audioPlayer = audioPlayer;
        this.audioController = audioController;
        this.videoPlayerView = videoPlayerView;
        this.videoController = videoController;
    }

    @Override
    public void start() {
        stopVideo();
        audioPlayer.start();
    }

    @Override
    public void pause() {
        audioPlayer.pause();
        audioController.show(0);
    }

    @Override
    public int getDuration() {
        return audioPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return audioPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        audioPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return audioPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return audioPlayer.getAudioSessionId();
    }
}
