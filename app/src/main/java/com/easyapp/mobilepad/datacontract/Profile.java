package com.easyapp.mobilepad.datacontract;

public class Profile {
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
}
