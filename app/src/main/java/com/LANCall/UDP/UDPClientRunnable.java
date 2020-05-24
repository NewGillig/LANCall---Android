package com.LANCall.UDP;

import android.os.Handler;

import com.LANCall.Network.Client;

import java.util.ArrayList;

public class UDPClientRunnable implements Runnable{
    public volatile boolean stop = false;
    private UDPClient client = null;
    private Handler mHandler = null;
    private boolean tryConnect = true;

    private ArrayList<byte[]> dataList = null;

    private int port = 0;
    private String ip = null;

    public UDPClientRunnable(UDPClient client, Handler mHandler, String ip, int port){
        this.client = client;
        this.mHandler = mHandler;
        this.ip = ip;
        this.port = port;

        dataList = new ArrayList<byte[]>();
    }

    public String getRemoteIP()
    {
        return client.getRemoteIP();
    }

    public byte[] readBytes()
    {
        if(dataList.size()>2)
            return client.readBytes();
        else
            return null;
    }

    public void writeBytes(byte[] data)
    {
        client.writeBytes(data);
    }

    @Override
    public void run() {
        while(!stop){
            dataList.add(client.readBytes());
        }
    }
}
