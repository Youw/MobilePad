package com.easyapp.mobilepad.datacontract;

public class Connection {
    public static final String TABLE_NAME = "Connections";
    public static final String PROFILE_ID = "profile_id";
    public static final String HOST = "host";
    public static final String PORT = "port";

    private final String mHost;
    private final int mPort;

    public Connection (String host, int port) {
        mHost = host;
        mPort = port;
    }

    public String getHost() { return mHost; }
    public int getPort() { return mPort; }

    @Override
    public String toString() { return mHost + ":" + String.valueOf(mPort); }
}
