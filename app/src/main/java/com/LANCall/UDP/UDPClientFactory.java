package com.LANCall.UDP;

import android.os.Handler;

import com.LANCall.Network.Client;
import com.LANCall.Network.ClientRunnable;

public class UDPClientFactory {
    public UDPClientFactory(){}

    public UDPClientRunnable createClient(String ip, int port, Handler mHandler){
        UDPClient client = new UDPClient();
        client.startClient(ip,port);

        UDPClientRunnable clientRunnable = new UDPClientRunnable(client,mHandler,ip,port);
        return clientRunnable;

    }
}
