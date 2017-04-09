package com.example.pc.myapplication.singletons;

public class PosVideoSingleton {

    private static PosVideoSingleton Singleton = null;

    private int position = 0;

    private Boolean isPlaying = null;

    private boolean haveToKeepPlaying = false;

    public static PosVideoSingleton getInstance() {
        if(Singleton == null)
        {
            Singleton = new PosVideoSingleton();
        }
        return Singleton;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean isPlaying() {
        if (isPlaying == null) {
            return false;
        }
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void clear() {
        position = 0;
        isPlaying = null;
        Singleton = null;
        haveToKeepPlaying = false;
    }

    public void setHaveToKeepPlaying(boolean haveTo) {
        haveToKeepPlaying = haveTo;
    }

    public boolean haveToKeepPlaying() {
        return haveToKeepPlaying;
    }
}
