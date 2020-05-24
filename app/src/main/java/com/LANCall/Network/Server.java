package com.LANCall.Network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    ServerSocket serverSocket = null;
    Socket socket = null;
    InputStream inputStream = null;
    //BufferedInputStream bufferedInputStream = null;
    InputStreamReader reader = null;
    OutputStream outputStream = null;
    BufferedReader bufReader = null;
    String str = null;
    StringBuffer stringBuffer = null;

    String remoteIP = null;
    boolean connected;





    public Server(){}

    public boolean startServer(int port){
        try {
            serverSocket = new ServerSocket(port);
        }catch(Exception e){
            return false;
        }
        Log.e("eee","Server Started");
        return true;
    }

    public boolean startListen(){
        if(serverSocket == null)
            return false;
        try {
            Log.e("eee","Listening port");
            socket = serverSocket.accept();
            Log.e("eee","server connected");
            connected = true;
            this.remoteIP = socket.getInetAddress().getHostName();
            inputStream = socket.getInputStream();
            //bufferedInputStream = new BufferedInputStream(inputStream);
            reader = new InputStreamReader(inputStream);
            bufReader = new BufferedReader(reader);
            stringBuffer = new StringBuffer();
            outputStream = socket.getOutputStream();

        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public byte[] getBytes(){

        byte[] data = new byte[640*32];
        byte[] datax = null;
        try {
            int len = inputStream.read(data);
            datax = Arrays.copyOfRange(data,0,len);
            Log.e("eee","Receive bytes: "+len);
            //Log.e("eee","buffer usage: "+inputStream.available());
            if(len<=0){return null;}
        }catch(Exception e){
            return null;
        }
        return datax;
    }

    public boolean writeBytes(byte[] data)
    {
        try {
            outputStream.write(data);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean getConnected()
    {
        return connected;
    }

    public String getRemoteIP()
    {
        return this.remoteIP;
    }


    public void shutdown()
    {
        try {
            serverSocket.close();
            socket.close();
            inputStream.close();
            reader.close();
            bufReader.close();
            outputStream.close();
        }catch(Exception e){}
    }




}
