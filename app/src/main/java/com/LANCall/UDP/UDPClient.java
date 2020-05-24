package com.LANCall.UDP;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class UDPClient {
    DatagramSocket socket = null;
    DatagramPacket packet = null;
    OutputStream outputStream = null;
    InputStream inputStream = null;
    InputStreamReader reader = null;
    BufferedReader bufReader = null;
    String str = null;
    String remoteIP = null;
    int remotePort = 0;

    public UDPClient(){}

    public boolean startClient(String ip,int port)
    {
        try {
            socket = new DatagramSocket();
            remoteIP = ip;
            this.remotePort = port;
           /* outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            reader = new InputStreamReader(inputStream);
            bufReader = new BufferedReader(reader);*/

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
            InetAddress address = InetAddress.getByName(remoteIP);
            DatagramPacket packet = new DatagramPacket(data,data.length,address,remotePort);
            socket.send(packet);

        }catch(Exception e){
        }
    }

    public byte[] readBytes()
    {
        byte[] data = new byte[640];
        try {
            DatagramPacket packet = new DatagramPacket(data,data.length);
            //Log.e("eee","getBytes: "+len);
            socket.receive(packet);
            if(packet.getLength()>0&&packet.getAddress().getHostAddress().equals(remoteIP))
                return packet.getData();
            else
                return null;
        }catch(Exception e){
            return null;
        }
    }
}
