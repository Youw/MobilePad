package com.easyapp.mobilepad;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Gasper on 14.12.2015.
 */
public class TCPSocketConnection extends Thread {

    public interface SocketListener {
        void onRead(String data);
    }

    private SocketListener mListener = null;
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
            mSocket = new Socket(mAddress, mPort);
            mListener.onRead("Connected to server");
            mIn = new DataInputStream(mSocket.getInputStream());
            mOut = new DataOutputStream(mSocket.getOutputStream());

            while (!Thread.currentThread().isInterrupted()) {
                if (mSendData.size() > 0) {
                    for (String data : mSendData) {
                        mOut.writeBytes(data);
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
            if (mSocket != null && !mSocket.isClosed()){
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        }
    }

    public void setListener(SocketListener listener) {
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