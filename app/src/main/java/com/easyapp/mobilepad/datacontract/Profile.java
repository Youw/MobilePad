package com.easyapp.mobilepad.datacontract;

import android.content.ContentValues;
import android.support.annotation.NonNull;

public class Profile implements DBSerializable {
    public static final String TABLE_NAME = "Profiles";

    public static final String ID = "id";
    public static final String USERNAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    private final int mId;
    private final String mName;
    private final String mEmail;
    private final byte[] mPasswordHash;

    public Profile(int id, String name, String email, byte[] passwordHash){
        mId = id;
        mName = name;
        mEmail = email;
        mPasswordHash = passwordHash;
    }

    public int getId(){ return mId; }
    public String getName() { return mName; }
    public String getEmail() { return mEmail; }
    public byte[] getPasswordHash() { return mPasswordHash; }

    @NonNull
    @Override
    public ContentValues getContent() {
        ContentValues values = new ContentValues(4);
        values.put(USERNAME, mName.toLowerCase());
        values.put(EMAIL, mEmail.toLowerCase());
        values.put(PASSWORD, mPasswordHash);
        if (mId != -1) values.put(ID, mId);
        return values;
    }

    @NonNull
    @Override
    public String getTableName() { return TABLE_NAME; }
}
