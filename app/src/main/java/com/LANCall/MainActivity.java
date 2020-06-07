package com.LANCall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.LANCall.Audio.AudioPlayer;
import com.LANCall.Audio.AudioPlayerFactory;
import com.LANCall.Audio.AudioPlayerRunnable;
import com.LANCall.Audio.AudioRecorder;
import com.LANCall.Audio.AudioRecorderFactory;
import com.LANCall.Audio.AudioRecorderRunnable;
import com.LANCall.Cipher.AESCipher;
import com.LANCall.Network.ClientFactory;
import com.LANCall.Network.ClientRunnable;
import com.LANCall.Network.ServerFactory;
import com.LANCall.Network.ServerRunnable;
import com.LANCall.UDP.UDPClient;
import com.LANCall.UDP.UDPClientFactory;
import com.LANCall.UDP.UDPClientRunnable;
import com.LANCall.UDP.UDPServerFactory;
import com.LANCall.UDP.UDPServerRunnable;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final int RECCODE = 1;
    final int WCODE = 2;
    final int RCODE = 3;



    private void checkPermission(){
        int hasMicrophone = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.RECORD_AUDIO);
        if(hasMicrophone != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("ccc","no permission");
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},RECCODE);
        }
        int hasWriteStorage = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(hasWriteStorage != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("ccc","no permission");
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WCODE);
        }
        int hasReadStorage = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(hasReadStorage != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("ccc","no permission");
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RCODE);
        }
    }
    @Override
    protected void onCreate(Bundle savadInstanceState)
    {

        super.onCreate(savadInstanceState);
/*        File keyFilex = null;
        keyFilex = new File(getFilesDir(),"key.ini");
        String keyHex;
        FileInputStream keyFileInput;
        AESCipher aesCipher = null;
        try{
            keyFileInput = new FileInputStream(keyFilex);
            byte[] keyHexByte = new byte[64];
            keyFileInput.read(keyHexByte,0,64);
            keyHex = new String(keyHexByte,"UTF-8");
            aesCipher = new AESCipher(keyHex);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        String msg = "awoif987875alsdfjoawiuefjlaksjfdlkajsdfl;k;jasd;lkf;";
        byte[] msgx = msg.getBytes();
        byte[] msgxx = new byte[104];
        System.arraycopy(msgx,0,msgxx,0,52);
        System.arraycopy(msgx,0,msgxx,52,52);
        byte[] xxx = aesCipher.encrypt(msgx);
        byte[] xxxx = aesCipher.encrypt(msgxx);
        byte[] xxx2 = new byte[104];
        System.arraycopy(xxx,0,xxx2,0,52);
        System.arraycopy(xxxx,0,xxx2,52,52);
        byte[] msgx2 = aesCipher.decrypt(xxx2);*/

        checkPermission();
        File keyFile = null;
        try {
            keyFile = new File(getFilesDir(),"key.ini");
            if(keyFile.exists()) {
                setContentView(R.layout.activity_main);
                findViewById(R.id.phoneCall).setOnClickListener(this);
                findViewById(R.id.Multi_phoneCall).setOnClickListener(this);
            }else
            {
                setContentView(R.layout.activity_main_new);
                findViewById(R.id.password_OK).setOnClickListener(this);
            }
        }catch(Exception e) {
            e.printStackTrace();

        }
        //findViewById(R.id.Multi_phoneCall).setOnClickListener(this);




/*        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                    }
                    r1 = audioRecorderFactory.createAudioRecoreder(mHandler);
                    r2 = audioRecorderFactory.createAudioRecoreder(mHandler);
                    record1 = new Thread(r1);
                    record2 = new Thread(r2);
                    record1.start();
                    record2.start();
                }
            }
        });*/





    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.phoneCall) {
            Intent intent = new Intent(this,VoipP2PActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(v.getId() == R.id.password_OK)
        {
            String password = ((TextView)findViewById(R.id.new_input_password)).getText().toString();
            FileOutputStream keyFile = null;
            try{
                keyFile = openFileOutput("key.ini",MODE_PRIVATE);
                keyFile.write(password.getBytes());
                keyFile.close();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
            setContentView(R.layout.activity_main);
            findViewById(R.id.phoneCall).setOnClickListener(this);
            findViewById(R.id.Multi_phoneCall).setOnClickListener(this);
        }
        if(v.getId() == R.id.Multi_phoneCall)
        {
            File file = null;
            try{
                file = new File(getFilesDir(),"key.ini");
                file.delete();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            this.finish();
        }

    }



}
