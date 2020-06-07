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
        stop=true;
        try{
            Thread.sleep(200);
        }catch(Exception e){}
        audioPlayer.shutdown();

    }
    @Override
    public void run() {
        while(!stop)
        {
            this.audioPlayer.playTone();
/*            try {
                Thread.sleep(1);
            }catch(Exception e){}*/
        }
    }
}
