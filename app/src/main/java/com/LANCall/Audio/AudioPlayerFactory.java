package com.LANCall.Audio;

import android.os.Handler;

public class AudioPlayerFactory {
    public AudioPlayerRunnable createAudioPlayer(Handler mHandler)          //Tell the factory the message handler
    {
        AudioPlayer audioPlayer = new AudioPlayer();
        audioPlayer.createPlayer();
        AudioPlayerRunnable audioPlayerRunnable = new AudioPlayerRunnable(audioPlayer);
        return audioPlayerRunnable;
    }
}
