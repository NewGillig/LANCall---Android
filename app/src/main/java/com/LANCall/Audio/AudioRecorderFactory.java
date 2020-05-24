package com.LANCall.Audio;


import android.os.Handler;

public class AudioRecorderFactory {
    public AudioRecorderRunnable createAudioRecoreder(Handler mHandler)
    {
        AudioRecorder audioRecorder = new AudioRecorder();
        audioRecorder.createAudio();
        AudioRecorderRunnable audioRecorderRunnable = new AudioRecorderRunnable(audioRecorder);
        return audioRecorderRunnable;

    }

}
