package com.LANCall.UDP;

import android.os.Handler;

import java.util.ArrayList;

public class UDPServerRunnable implements Runnable{
    public volatile boolean stop = false;
    private UDPServer server = null;
    private Handler mHandler = null;
    private boolean tryConnect = true;

    private ArrayList<byte[]> dataList = null;

    private int port = 0;

    public UDPServerRunnable(UDPServer server, Handler mHandler, int port){
        this.server = server;
        this.mHandler = mHandler;
        this.port = port;

        dataList = new ArrayList<byte[]>();
    }

    public String getRemoteIP()
    {
        return server.getRemoteIP();
    }

    public byte[] readBytes()
    {
        if(dataList.size()>2)
            return dataList.remove(0);
        else
            return null;
    }

    public void writeBytes(byte[] data)
    {
        server.writeBytes(data);
    }

    @Override
    public void run() {
        while(!stop){
            dataList.add(server.readBytes());
        }
    }
}
