package com.LANCall.UDP;

import android.os.Handler;

public class UDPServerFactory {
    public UDPServerFactory(){}

    public UDPServerRunnable createServer(int port, Handler mHandler){
        UDPServer server = new UDPServer();
        server.startServer(port);

        UDPServerRunnable serverRunnable = new UDPServerRunnable(server,mHandler,port);
        return serverRunnable;

    }
}
