package com.LANCall.Audio;


import android.util.Log;

public class AudioPlayerRunnable implements Runnable{
    public volatile boolean stop = false;
    private AudioPlayer audioPlayer = null;
    public AudioPlayerRunnable(AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
    }
    public void addData(byte[] data)
    {
        this.audioPlayer.addData(data);
    }
    public void shutdown(){
        audioPlayer.shutdown();
        stop=true;
    }
    @Override
    public void run() {
        while(!stop)
        {
            this.audioPlayer.playTone();
        }
    }
}
