package com.LANCall.Audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


public class AudioRecorder {
    //private static AudioRecorder audioRecorder = null;
    private final static int AUDIO_SAMPLE_RATE = 8000;
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_8BIT;
    private int bufferSize = 0;
    private AudioRecord audioRecord = null;

    private byte[] audioData = null;

    private ArrayList<byte[]> dataList = null;

    public AudioRecorder(){}
/*
    public static AudioRecorder getInstance(){
        if(audioRecorder == null)
        {
            audioRecorder = new AudioRecorder();
        }
        return audioRecorder;
    }
*/
    public void createAudio()
    {
        bufferSize = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE,AUDIO_CHANNEL,AUDIO_ENCODING);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,AUDIO_SAMPLE_RATE,AUDIO_CHANNEL,AUDIO_ENCODING,32);
        audioData = new byte[32];
        dataList = new ArrayList<byte[]>();
        audioRecord.startRecording();
    }
    public void recordTone()
    {
/*        try {
            Thread.sleep(200);
        }catch(Exception e){}*/

        //Log.e("eee","list size"+dataList.size());
        int read = audioRecord.read(audioData, 0, 16);
        Log.e("eee","record bytes"+read);
        byte[] audiox = null;
        if(read>0) {
            audiox = Arrays.copyOfRange(audioData, 0, read);
            dataList.add(audiox);
        }
        if(dataList.size()>8)
        {
            while(dataList.size()>2)
            {
                dataList.remove(0);
            }
        }



    }
    public ArrayList<byte[]> getDataList(){
        return dataList;
    }
    public byte[] fetchAudioData(){
        if(dataList.size()>2) {
            return dataList.remove(0);
        }
        else {
/*            try {
                Thread.sleep(200);
            }catch(Exception e){}*/
            return null;
        }
    }

    public void shutdown(){
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
    }


}
