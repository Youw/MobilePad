package com.easyapp.mobilepad.datacontract;

import android.content.ContentValues;
import android.support.annotation.NonNull;

public class Preset implements DBSerializable {
    public static final String TABLE_NAME = "Presets";
    public static final String PROFILE_ID = "profile_id";
    public static final String NAME = "name";
    public static final String PRESET = "preset";

    private final int mProfileId;
    private final String mName;
    private final String mPreset;

    public Preset(String name, String preset){
        mName = name;
        mPreset = preset;
        mProfileId = -1;
    }

    public Preset(int profileId, String name, String preset){
        mProfileId = profileId;
        mName = name;
        mPreset = preset;
    }

    public String getName() { return mName; }
    public String getPreset() { return mPreset; }

    @Override
    @NonNull
    public ContentValues getContent() {
        ContentValues values = new ContentValues(3);
        values.put(PROFILE_ID, mProfileId);
        values.put(NAME, mName);
        values.put(PRESET, mPreset);
        return values;
    }

    @Override
    @NonNull
    public String getTableName() { return TABLE_NAME; }
}

