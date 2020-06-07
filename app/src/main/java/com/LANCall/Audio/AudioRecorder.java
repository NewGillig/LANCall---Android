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
    private int count = 0;
    private AudioRecord audioRecord = null;

    private byte[] audioData = null;
    byte[] audiox = new byte[2000];

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
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,AUDIO_SAMPLE_RATE,AUDIO_CHANNEL,AUDIO_ENCODING,4000);
        audioData = new byte[2000];
        dataList = new ArrayList<byte[]>();
        audioRecord.startRecording();
    }
    public void recordTone()
    {
/*        try {
            Thread.sleep(200);
        }catch(Exception e){}*/

        //Log.e("eee","list size"+dataList.size());
        int read = audioRecord.read(audioData, 0, 2000);
        if(read>0) {
            audiox = Arrays.copyOfRange(audioData, 0, read);
            dataList.add(audiox);
        }
        //Log.e("eee","record bytes"+read);

/*        if(read>0) {
            System.arraycopy(audioData,0,audiox,count,read);
            count+=read;
            //audiox = Arrays.copyOfRange(audioData, 0, read);
        }
        if(count>=1900)
        {
            byte[] audioxx = Arrays.copyOfRange(audiox,0,count);
            dataList.add(audioxx);
            count=0;
        }*/
        if(dataList.size()>4)
        {
            while(dataList.size()>1)
            {
                dataList.remove(0);
            }
        }



    }
    public ArrayList<byte[]> getDataList(){
        return dataList;
    }
    public byte[] fetchAudioData(){
        if(dataList.size()>1) {
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
