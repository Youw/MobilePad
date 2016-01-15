package com.easyapp.mobilepad;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Gasper on 14.12.2015.
 */
public class TCPSocketConnection extends Thread implements Serializable {

    public interface TCPConnectionListener {
        void onRead(String data);
        void onConnected(boolean connected);
        void onDisconnected();
    }

    private TCPConnectionListener mListener = null;
    private final static int PORT = 8887;
    private final String mAddress;
    private final int mPort;

    private Socket mSocket = null;
    private DataInputStream mIn = null;
    private DataOutputStream mOut = null;

    private Semaphore semaphoreReadSendData = new Semaphore(0);
    private Semaphore semaphoreWriteSendData = new Semaphore(0);

    private List<String> mSendData = new ArrayList<>();

    public TCPSocketConnection(String addr, int port) {
        mAddress = addr;
        mPort = port;
    }

    public TCPSocketConnection(String addr) {
        mAddress = addr;
        mPort = PORT;
    }

    @Override
    public void run() {
        try {
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(mAddress, mPort), 3000);
            mListener.onConnected(true);
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onConnected(false);
            return;
        }
        try {
            mIn = new DataInputStream(mSocket.getInputStream());
            mOut = new DataOutputStream(mSocket.getOutputStream());

            while (!Thread.currentThread().isInterrupted()) {
                if (mSendData.size() > 0) {
                    for (String data : mSendData) {
                        mOut.writeBytes(data+"\n");
                    }
                    mOut.flush();
                    mSendData.clear();
                }
                if (mIn.available() > 0) {
                    mListener.onRead(mIn.readUTF());
                }

                semaphoreWriteSendData.release();
                semaphoreReadSendData.acquire();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mIn != null) {
                try {
                    mIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mOut != null) {
                try {
                    mOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mSocket != null && !mSocket.isClosed()){
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mListener.onDisconnected();
        }
    }

    public void setListener(TCPConnectionListener listener) {
        this.mListener = listener;
    }

    public void send(String data){
        try {
            semaphoreWriteSendData.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        this.mSendData.add(data);
        semaphoreReadSendData.release();
    }
}