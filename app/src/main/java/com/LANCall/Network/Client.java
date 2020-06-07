package com.LANCall.Network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    Socket socket = null;
    OutputStream outputStream = null;
    InputStream inputStream = null;
    InputStreamReader reader = null;
    BufferedReader bufReader = null;
    String str = null;
    String remoteIP = null;

    public Client(){}

    public boolean startClient(String ip,int port)
    {
        Log.e("eee","Try to connect to: "+ip+":"+port);
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip,port),20000);
            remoteIP = ip;
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            reader = new InputStreamReader(inputStream);
            bufReader = new BufferedReader(reader);

        }catch(Exception e){
            return false;
        }
        return true;
    }

    public String getRemoteIP()
    {
        return remoteIP;
    }

    public void writeBytes(byte[] data)
    {
        try {
            outputStream.write(data);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public byte[] readBytes()
    {
        byte[] data = new byte[640*640];
        try {
            int len = inputStream.read(data);
            //Log.e("eee","getBytes: "+len);
            if(len>0)
                return Arrays.copyOfRange(data,0,len);
            else
                return null;
        }catch(Exception e){
            return null;
        }
    }

    public void shutdown()
    {
        try {
            socket.close();
            inputStream.close();
            reader.close();
            bufReader.close();
            outputStream.close();
            socket = null;
            inputStream = null;
            reader = null;
            bufReader = null;
            outputStream = null;
        }catch(Exception e){}
    }

}
