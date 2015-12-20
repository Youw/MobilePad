package com.easyapp.mobilepad;

import android.support.annotation.NonNull;

import com.easyapp.mobilepad.datacontract.*;

import java.util.List;

/**
 * Created by Gasper on 15.12.2015.
 */
public interface DBConnection {
    Profile getProfileByUsername(@NonNull String username);
    Profile getProfileByEmail(@NonNull String email);
    List<Connection> getConnections(int profileId);
    List<Preset> getPresets(int profileId);

    boolean insert(@NonNull DBSerializable obj);
    boolean createProfile(String username, String email, byte passwordHash[]);
}

