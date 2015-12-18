package com.easyapp.mobilepad.datacontract;

import android.content.ContentValues;
import android.support.annotation.NonNull;

public class Profile implements DBSerializable {
    public static final String TABLE_NAME = "Profiles";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";

    private final int mId;
    private final String mName;
    private final byte[] mPassword;

    public Profile(int id, String name, byte[] password){
        mId = id;
        mName = name;
        mPassword = password;
    }

    public int getId(){ return mId; }
    public String getName() { return mName; }
    public byte[] getPassword() { return mPassword; }

    @NonNull
    @Override
    public ContentValues getContent() {
        ContentValues values = new ContentValues(3);
        values.put(NAME, mName);
        values.put(PASSWORD, mPassword);
        if (mId != -1) values.put(ID, mId);
        return values;
    }

    @NonNull
    @Override
    public String getTableName() { return TABLE_NAME; }
}
