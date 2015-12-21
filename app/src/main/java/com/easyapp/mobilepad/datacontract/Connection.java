package com.easyapp.mobilepad.datacontract;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class Connection implements DBSerializable, Serializable {
    public static final String TABLE_NAME = "Connections";
    public static final String PROFILE_ID = "profile_id";
    public static final String HOST = "host";
    public static final String PORT = "port";

    private final int mProfileId;
    private final String mHost;
    private final int mPort;

    public Connection (String host, int port) {
        mHost = host;
        mPort = port;
        mProfileId = -1;
    }

    public Connection (int profileId, String host, int port){
        mProfileId = profileId;
        mHost = host;
        mPort = port;
    }

    public String getHost() { return mHost; }
    public int getPort() { return mPort; }

    @Override
    public String toString() { return mHost + ":" + String.valueOf(mPort); }

    @NonNull
    @Override
    public ContentValues getContent() {
        ContentValues values = new ContentValues(3);
        if (mProfileId != -1) {
            values.put(PROFILE_ID, mProfileId);
        }
        values.put(HOST, mHost);
        values.put(PORT, mPort);
        return values;
    }

    @NonNull
    @Override
    public String getTableName() { return TABLE_NAME; }


}
