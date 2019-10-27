package org.tulg.roundback.master;

import org.tulg.roundback.core.NetIOHandler;
import org.tulg.roundback.core.RoundBackConfig;

import java.io.*;
import java.net.Socket;

/**
 * Created by jasonw on 10/7/2016.
 */
class MasterThread implements Runnable {
    private Socket clientSock;
    private boolean quitting;
    private final MasterProtocol mProto;
    private NetIOHandler netIOHandler = null;
    private RoundBackConfig rBackConfig = null;

    public MasterThread(Socket clientSock){
        this.clientSock = clientSock;
        quitting = false;
        netIOHandler = new NetIOHandler();
        mProto = new MasterProtocol(netIOHandler);

    }

    public void setClientSock(Socket clientSock) {
        this.clientSock = clientSock;
    }


    public void setRoundBackConfig(RoundBackConfig rBackConfig) {
        this.rBackConfig = rBackConfig;
        mProto.setRoundBackConfig(rBackConfig);

    }

    @Override
    public void run() {
        netIOHandler.setEncrypted(rBackConfig.getEncrypted());
        netIOHandler.setEncryptionKey(rBackConfig.getEncryptionKey());
        netIOHandler.setClientAddress(clientSock.getInetAddress().getHostAddress());
        try {
            netIOHandler.setIn(clientSock.getInputStream());
            netIOHandler.setOut(clientSock.getOutputStream());
            netIOHandler.println("RoundBack Master Server");
        } catch (IOException e) {
            System.err.println("Error: Cannot open input stream");
            quitting = true;
        }


        String inputLine;
        // listen until we quit
        while(!quitting) {
            try {

                    inputLine = netIOHandler.readLine();
                    if(!mProto.process(inputLine)){
                        quitting=true;
                    }

            } catch (IOException e) {
                System.err.println("Exception: " + e.getMessage());
                quitting = true;
            }


        }
        netIOHandler.close();
        try {
            clientSock.close();
        } catch (IOException e) {
            // ignore exception on close.
        }

    }
}
