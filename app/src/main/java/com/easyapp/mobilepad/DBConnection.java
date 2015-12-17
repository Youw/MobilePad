package com.easyapp.mobilepad;

import android.support.annotation.NonNull;

import com.easyapp.mobilepad.datacontract.*;

import java.util.List;

/**
 * Created by Gasper on 15.12.2015.
 */
public interface DBConnection {
    Profile getProfile(@NonNull String login);
    List<Connection> getConnections(int profileId);
    List<Preset> getPresets(int profileId);
    boolean updateDb(Connection connection);
}

