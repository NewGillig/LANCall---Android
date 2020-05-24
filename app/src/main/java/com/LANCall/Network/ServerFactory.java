package com.LANCall.Network;

import android.os.Handler;

public class ServerFactory {
    public ServerFactory(){}

    public ServerRunnable createServer(int port, Handler mHandler)
    {
        Server server = new Server();
        if(server.startServer(port)){
            ServerRunnable serverRunnable = new ServerRunnable(server, mHandler);
            return serverRunnable;
        }
        else
            return null;

    }

}
