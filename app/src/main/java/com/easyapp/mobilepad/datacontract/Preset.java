package com.easyapp.mobilepad.datacontract;

public class Preset {
    public static final String TABLE_NAME = "Presets";
    public static final String PROFILE_ID = "profile_id";
    public static final String NAME = "name";
    public static final String PRESET = "preset";

    private final String mName;
    private final String mPreset;

    public Preset(String name, String preset){
        mName = name;
        mPreset = preset;
    }

    public String getName() { return mName; }
    public String getPreset() { return mPreset; }
}

