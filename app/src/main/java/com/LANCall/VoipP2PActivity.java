package com.LANCall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.LANCall.Audio.AudioPlayerFactory;
import com.LANCall.Audio.AudioPlayerRunnable;
import com.LANCall.Audio.AudioRecorderFactory;
import com.LANCall.Audio.AudioRecorderRunnable;
import com.LANCall.Cipher.AESCipher;
import com.LANCall.Network.ClientFactory;
import com.LANCall.Network.ClientRunnable;
import com.LANCall.Network.NetUtil;
import com.LANCall.Network.ServerFactory;
import com.LANCall.Network.ServerRunnable;
import com.LANCall.UDP.UDPClientFactory;
import com.LANCall.UDP.UDPClientRunnable;
import com.LANCall.UDP.UDPServerFactory;
import com.LANCall.UDP.UDPServerRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class VoipP2PActivity extends AppCompatActivity implements View.OnClickListener{

    final int recvCall = 100;
    final int startTalk = 200;



    final byte[] acceptCall = {1,8,9,3,1,2,2,6};
    final byte[] endCall = {1,9,0,4,0,8,2,2};


    Thread record = null;
    Thread play = null;

    boolean talking = false;
    boolean runMsgListener = true;

    boolean isClient = false;


    AudioRecorderRunnable r = null;
    AudioPlayerRunnable p = null;

    AudioPlayerFactory audioPlayerFactory = null;
    AudioRecorderFactory audioRecorderFactory = null;

    ClientFactory clientFactory = null;
    ServerFactory serverFactory = null;

    ClientRunnable voiceClient = null;
    ServerRunnable voiceServer = null;

    Thread tVoiceClient = null;
    Thread tVoiceServer = null;

    ClientRunnable msgClient = null;
    ServerRunnable msgServer = null;

    Thread tMsgClient = null;
    Thread tMsgServer = null;

    Runnable msgListener = null;

    String localIP = null;
    String remoteIP = null;

    String keyHex = null;

    AESCipher aesCipher = null;


    int page = 0;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == recvCall)
            {
                isClient = false;
                showRinging(msg);
            }
            else if(msg.what == startTalk)
            {
                startTalking();
            }
        }
    };

    private void showRinging(Message msg)
    {
        setContentView(R.layout.activity_voip_p2p_ringing);
        ((TextView)findViewById(R.id.ring_targetid_text)).setText((String)msg.obj);
        remoteIP = (String)msg.obj;
        findViewById(R.id.ring_pickup).setOnClickListener(this);
        findViewById(R.id.ring_hang_off).setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File keyFile = null;
        keyFile = new File(getFilesDir(),"key.ini");
        FileInputStream keyFileInput;
        try{
            keyFileInput = new FileInputStream(keyFile);
            byte[] keyHexByte = new byte[64];
            keyFileInput.read(keyHexByte,0,64);
            keyHex = new String(keyHexByte,"UTF-8");
            aesCipher = new AESCipher(keyHex);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_voip_p2p_init);
        page = 1;
        findViewById(R.id.user_input_phoneCall).setOnClickListener(this);

        localIP = NetUtil.getIPAddress(this);

        ((TextView)findViewById(R.id.local_ip_addr)).setText(localIP);

        audioPlayerFactory = new AudioPlayerFactory();
        audioRecorderFactory = new AudioRecorderFactory();
        r = audioRecorderFactory.createAudioRecoreder(mHandler);
        p = audioPlayerFactory.createAudioPlayer(mHandler);

        record = new Thread(r);
        play = new Thread(p);


        clientFactory = new ClientFactory();
        serverFactory = new ServerFactory();

        voiceServer = serverFactory.createServer(28888,mHandler);
        msgServer = serverFactory.createServer(27777,mHandler);

        tVoiceServer = new Thread(voiceServer);
        tVoiceServer.start();

        tMsgServer = new Thread(msgServer);
        tMsgServer.start();

        msgListener = new Runnable() {
            @Override
            public void run() {
                while(runMsgListener) {
                    try {
                        Thread.sleep(1000);
                    }catch(Exception e){}
                    if(!isClient) {
                        if (msgServer != null) {
                            byte[] data = msgServer.getBytes();
                            if (data == null)
                                continue;
                            else
                            {
                                if(Arrays.equals(data,endCall))
                                {
                                    restartP2P();
                                }
                                else if(Arrays.equals(data,acceptCall))
                                {
                                    mHandler.sendEmptyMessage(startTalk);
                                }
                            }
                        }
                    }
                    else
                    {
                        if(msgClient!=null) {
                            byte[] data = msgClient.readBytes();
                            if(data == null)
                            {
                                continue;
                            }
                            else
                            {
                                if(Arrays.equals(data,endCall))
                                {
                                    restartP2P();
                                }
                                else if(Arrays.equals(data,acceptCall))
                                {
                                    mHandler.sendEmptyMessage(startTalk);
                                }
                            }
                        }
                    }

                }
            }
        };
        new Thread(msgListener).start();

        msgListener = new Runnable() {
            @Override
            public void run() {
                while(runMsgListener) {
                    try {
                        Thread.sleep(1000);
                    }catch(Exception e){}
                    if(!isClient) {
                        if (msgServer != null) {
                            byte[] data = msgServer.getBytes();
                            if (data == null)
                                continue;
                            else
                            {
                                if(Arrays.equals(data,endCall))
                                {
                                    restartP2P();
                                }
                                else if(Arrays.equals(data,acceptCall))
                                {
                                    mHandler.sendEmptyMessage(startTalk);
                                }
                            }
                        }
                    }
                    else
                    {
                        if(msgClient!=null) {
                            byte[] data = msgClient.readBytes();
                            if(data == null)
                            {
                                continue;
                            }
                            else
                            {
                                if(Arrays.equals(data,endCall))
                                {
                                    restartP2P();
                                }
                                else if(Arrays.equals(data,acceptCall))
                                {
                                    mHandler.sendEmptyMessage(startTalk);
                                }
                            }
                        }
                    }

                }
            }
        };
        new Thread(msgListener).start();
    }

    public void startTalking()
    {
        talking = true;
        if(isClient&&voiceClient==null)
            return;
        if(!isClient&&!voiceServer.getConnected())
            return;

        setContentView(R.layout.activity_voip_p2p_talking);
        ((TextView)findViewById(R.id.talk_targetid_text)).setText(remoteIP);
        findViewById(R.id.talking_hangup).setOnClickListener(this);
        record.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(talking)
                {
                    byte[] out = r.fetchTone();
                    if(out!=null) {
                        if(isClient) {
                            byte[] cipherText = aesCipher.encrypt(out);
                            byte[] cipherTextx = new byte[cipherText.length+18];
                            String head = "123456789";
                            String end = "987654321";
                            System.arraycopy(head.getBytes(),0,cipherTextx,0,9);
                            System.arraycopy(cipherText,0,cipherTextx,9,cipherText.length);
                            System.arraycopy(end.getBytes(),0,cipherTextx,cipherText.length+9,9);
                            voiceClient.writeBytes(cipherTextx);
                        }
                        else {
                            byte cipherText[] = aesCipher.encrypt(out);
                            byte[] cipherTextx = new byte[cipherText.length+18];
                            String head = "123456789";
                            String end = "987654321";
                            System.arraycopy(head.getBytes(),0,cipherTextx,0,9);
                            System.arraycopy(cipherText,0,cipherTextx,9,cipherText.length);
                            System.arraycopy(end.getBytes(),0,cipherTextx,cipherText.length+9,9);
                            voiceServer.writeBytes(cipherTextx);
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] cipherDataBuf = new byte[200000];
                int place=0;
                while(talking)
                {
                    try {
                        Thread.sleep(1);
                    }catch(Exception e){}
                    byte[] data = null;

                    if(isClient) {
                        byte[] cipherData = voiceClient.readBytes();
                        if(cipherData!=null) {
                            System.arraycopy(cipherData, 0, cipherDataBuf, place, cipherData.length);
                            place += cipherData.length;
                        }
                        //data = aesCipher.decrypt(cipherData);
                    }
                    else {
                        byte[] cipherData = voiceServer.getBytes();
                        if(cipherData!=null) {
                            System.arraycopy(cipherData, 0, cipherDataBuf, place, cipherData.length);
                            place += cipherData.length;
                        }
                        //data = aesCipher.decrypt(cipherData);
                    }
                    int start=-1;
                    for(int i=0;i<place-9;i++)
                    {
                        if(cipherDataBuf[i]=='1'&&cipherDataBuf[i+1]=='2'&&cipherDataBuf[i+2]=='3'&&cipherDataBuf[i+3]=='4'&&cipherDataBuf[i+4]=='5'&&cipherDataBuf[i+5]=='6'&&cipherDataBuf[i+6]=='7'&&cipherDataBuf[i+7]=='8'&&cipherDataBuf[i+8]=='9')
                        {
                            start=i+9;
                        }
                        if(start!=-1&&cipherDataBuf[i]=='9'&&cipherDataBuf[i+1]=='8'&&cipherDataBuf[i+2]=='7'&&cipherDataBuf[i+3]=='6'&&cipherDataBuf[i+4]=='5'&&cipherDataBuf[i+5]=='4'&&cipherDataBuf[i+6]=='3'&&cipherDataBuf[i+7]=='2'&&cipherDataBuf[i+8]=='1'){
                            data = aesCipher.decrypt(Arrays.copyOfRange(cipherDataBuf,start,i));
/*                            if(data[10]!=127&&data[10]!=-128)
                            {
                                Log.e("eee","noise");
                            }*/
                            System.arraycopy(cipherDataBuf,i+9,cipherDataBuf,0,place-i-9);
                            //cipherDataBuf = Arrays.copyOfRange(cipherDataBuf,i+3,place);
                            place-=i+9;
                            break;
                        }
                    }
                    if(place>180000)
                    {
                        place = 0;
                    }


                    if(data!=null) {
                        Log.e("eee","writeData"+data.length);
                        p.addData(data);

                        //Log.e("eee","playBytes: "+data.length);
                    }
                }
            }
        }).start();

        play.start();

    }

    public void restartP2P()
    {
        talking = false;
        runMsgListener = false;
        try {
            r.shutdown();
        }catch(Exception e){}
        try {
            p.shutdown();
        }catch (Exception e){}
        r=null;
        p=null;
        try {
            msgServer.shutdown();
        }catch (Exception e){}
        try {
            voiceServer.shutdown();
        }catch (Exception e){}
        try {
            msgClient.shutdown();
        }catch (Exception e){}
        try {
            voiceClient.shutdown();
        }catch (Exception e){}
        msgServer = null;
        voiceServer = null;
        Intent intent = new Intent(this,VoipP2PActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.user_input_phoneCall)
        {
            String ip = ((TextView)findViewById(R.id.user_input_TargetIp)).getText().toString();
            msgClient = clientFactory.createClient(ip,27777,mHandler);
            tMsgClient = new Thread(msgClient);
            tMsgClient.start();
            SystemClock.sleep(100);
            while(msgClient.getConnected()==0){
                SystemClock.sleep(100);
            }
            if(msgClient.getConnected() == 1) {
                voiceClient = clientFactory.createClient(ip, 28888, mHandler);
                tVoiceClient = new Thread(voiceClient);
                SystemClock.sleep(100);
                tVoiceClient.start();
                SystemClock.sleep(100);
                while(voiceClient.getConnected()==0){
                    SystemClock.sleep(100);
                }
            }


            if(msgClient==null||voiceClient==null||msgClient.getConnected()!=1||voiceClient.getConnected()!=1)
            {
                ((TextView)findViewById(R.id.ip_prompt)).setText("Connect failed. Please input correct IP.");
                if (msgClient != null) {
                    msgClient.stop = true;
                    msgClient.shutdown();
                    msgClient = null;
                    tMsgClient = null;
                }
                if(voiceClient!=null) {
                    voiceClient.stop = true;
                    voiceClient.shutdown();
                    voiceClient = null;
                    tVoiceClient = null;
                }
            }
            else
            {
                isClient = true;
                setContentView(R.layout.activity_voip_p2p_calling);
                ((TextView)findViewById(R.id.targetid_text)).setText(ip);
                remoteIP = ip;
                findViewById(R.id.calling_hangup).setOnClickListener(this);
            }
        }

        if(v.getId() == R.id.calling_hangup){
            if(isClient)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgClient.writeBytes(endCall);
                    }
                }).start();
                SystemClock.sleep(1000);
                restartP2P();
            }
            else
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgServer.writeBytes(endCall);
                    }
                }).start();
                SystemClock.sleep(1000);
                restartP2P();
            }
        }

        if(v.getId() == R.id.ring_hang_off){
            if(isClient)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgClient.writeBytes(endCall);
                    }
                }).start();
                SystemClock.sleep(1000);
                restartP2P();
            }
            else
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgServer.writeBytes(endCall);
                    }
                }).start();
                SystemClock.sleep(1000);
                restartP2P();
            }
        }

        if(v.getId() == R.id.talking_hangup){
            if(isClient)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgClient.writeBytes(endCall);
                    }
                }).start();
                SystemClock.sleep(1000);
                restartP2P();
            }
            else
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgServer.writeBytes(endCall);
                    }
                }).start();
                SystemClock.sleep(1000);
                restartP2P();
            }
        }

        if(v.getId() == R.id.ring_pickup)
        {
            if(isClient)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgClient.writeBytes(acceptCall);
                    }
                }).start();
                SystemClock.sleep(1000);
                startTalking();
            }
            else
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        msgServer.writeBytes(acceptCall);
                    }
                }).start();
                SystemClock.sleep(1000);
                startTalking();
            }
        }

       /* record1.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    byte[] out = r1.fetchTone();
                    if(out!=null) {
                        c.writeBytes(out);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    byte[] data = c.readBytes();
                    if(data!=null) {
                        p1.addData(data);
                        Log.e("eee","playBytes: "+data.length);
                    }
                }
            }
        }).start();
        play1.start();

        if(v.getId() == R.id.phoneCall2)
        {
            c = clientFactory.createClient("10.8.0.18",27777,mHandler);
            client = new Thread(c);
            client.start();
        }

        if(v.getId() == R.id.Multi_phoneCall)
        {
            record1.start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true)
                    {
                        byte[] out = r1.fetchTone();

                        if(out!=null) {
                            //Log.e("eee","getbytes: "+out.length);
                            s.writeBytes(out);
                        }
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true)
                    {
                        *//*try {
                            Thread.sleep(200);
                        }catch(Exception e){}*//*
                        byte[] data = s.getBytes();
                        if(data!=null) {

                            p2.addData(data);

                        }
                    }
                }
            }).start();
            play2.start();
        }*/


    }
}
