package com.LANCall.Network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerRunnable implements Runnable{
    public volatile boolean stop = false;
    private Server server = null;
    private Handler mHandler = null;
    private boolean connected = false;

    public ServerRunnable(Server server, Handler mHandler)
    {
        this.server = server;
        this.mHandler = mHandler;
    }


    public byte[] getBytes()
    {
        return this.server.getBytes();
    }

    public void writeBytes(byte[] data){server.writeBytes(data);}

    public String getRemoteIP()
    {
        return server.getRemoteIP();
    }

    @Override
    public void run() {
        while(!stop){
            if(!connected)
            {
                Log.e("eee","Start Listening");
                if(this.server.startListen()) {
                    this.connected = true;
                    Message msg = new Message();
                    msg.obj = getRemoteIP();
                    msg.what = 100;
                    this.mHandler.sendMessage(msg);
                    Log.e("eee","Connected");
                }
            }
            else
            {

            }
        }
    }

    public boolean getConnected()
    {
        return server.getConnected();
    }

    public void shutdown()
    {
        stop = true;
        server.shutdown();
    }

}
