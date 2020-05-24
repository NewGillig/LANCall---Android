package com.LANCall.Audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.ArrayList;

public class AudioPlayer {
    //private static AudioPlayer audioPlayer = null;
    private final static int AUDIO_SAMPLE_RATE = 8000;
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_8BIT;
    private int bufferSize = 0;
    private AudioTrack audioTrack = null;

    private byte[] audioData = null;


    private ArrayList<byte[]> dataList = null;

    public AudioPlayer(){}
/*
    public static AudioPlayer getInstance(){
        if(audioPlayer == null)
        {
            audioPlayer = new AudioPlayer();
        }
        return audioPlayer;
    }
*/
    public void createPlayer()
    {
        dataList = new ArrayList<byte[]>();
        bufferSize = AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE,AUDIO_CHANNEL,AUDIO_ENCODING);
        audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL,AUDIO_SAMPLE_RATE,AUDIO_CHANNEL,AUDIO_ENCODING,bufferSize*32,AudioTrack.MODE_STREAM);
        audioTrack.play();
        audioData = new byte[bufferSize];
    }
    public void playTone()
    {

        //len = audioTrack.getPlaybackHeadPosition();
        //if(dataList.size()>200)
        //    audioTrack.flush();
        if(getBufferLen()>8000)
        {
            //audioTrack.flush();
            while(getBufferLen()>4000)
                dataList.remove(0);
        }

        if(getBufferLen()>4000) {
            while(dataList.size()>1) {
                audioData = dataList.remove(0);
                if (audioData != null) {
                    audioTrack.write(audioData, 0, audioData.length);
                    //audioTrack.stop();
                }
            }
        }
    }

    public int getBufferLen()
    {
        int len=0;
        for(int i=0;i<dataList.size();i++)
        {
            if(dataList!=null) {
                if(dataList.get(i)!=null)
                    len += dataList.get(i).length;
            }
        }
        return len;
    }

    public void addData(byte[] data)
    {
        if(data!=null)
            dataList.add(data);
    }

    public void shutdown(){
        audioTrack.stop();
        audioTrack.flush();
        audioTrack.release();
        audioTrack = null;
    }

}
