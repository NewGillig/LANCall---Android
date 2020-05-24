package com.LANCall.Network;

import android.os.Handler;
import android.os.Message;

public class ClientRunnable implements Runnable{
    public volatile boolean stop = false;
    private Client client = null;
    private Handler mHandler = null;
    private int connected = 0;

    private int port = 0;
    private String ip = null;

    public ClientRunnable(Client client, Handler mHandler, String ip, int port){
        this.client = client;
        this.mHandler = mHandler;
        this.ip = ip;
        this.port = port;
    }

    public String getRemoteIP()
    {
        return client.getRemoteIP();
    }

    public byte[] readBytes()
    {
        return client.readBytes();
    }

    public int getConnected()
    {
        return connected;
    }

    public void writeBytes(byte[] data)
    {
        client.writeBytes(data);
    }

/*    public boolean connect(){
        if(client.startClient(ip,port))
        {
            connected = true;
            return true;
        }
        return false;
    }*/

    @Override
    public void run() {
        if(connected == 0)
        {
            if(client.startClient(ip,port)) {
                connected = 1;
            }
            else
            {
                connected = -1;
            }
        }
        while(!stop){

        }
    }

    public void shutdown()
    {
        client.shutdown();
        stop=true;
    }
}
