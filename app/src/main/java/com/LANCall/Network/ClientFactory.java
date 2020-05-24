package com.LANCall.Network;

import android.os.Handler;

public class ClientFactory {
    public ClientFactory(){}

    public ClientRunnable createClient(String ip, int port, Handler mHandler){
        Client client = new Client();

        ClientRunnable clientRunnable = new ClientRunnable(client,mHandler,ip,port);
        return clientRunnable;

    }
}
