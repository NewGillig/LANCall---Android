package com.LANCall.Audio;

public class AudioRecorderRunnable implements Runnable{
    public volatile boolean stop = false;
    private AudioRecorder audioRecorder = null;

    public AudioRecorderRunnable(AudioRecorder audioRecorder){
        this.audioRecorder = audioRecorder;
    }

    public byte[] fetchTone()
    {
        return audioRecorder.fetchAudioData();
    }
    public void shutdown(){
        audioRecorder.shutdown();
        stop=true;
    }

    @Override
    public void run() {
        while(!stop){
            audioRecorder.recordTone();
        }
    }
}
