package com.easyapp.mobilepad;

import android.support.annotation.NonNull;

import com.easyapp.mobilepad.datacontract.*;

import java.util.List;

/**
 * Created by Gasper on 15.12.2015.
 */
public interface DBConnection {
    int validateProfile(@NonNull String login, @NonNull byte[] pass);
    List<Connection> getConnections(int profileId);
    List<Preset> getPresets(int profileId);
}

