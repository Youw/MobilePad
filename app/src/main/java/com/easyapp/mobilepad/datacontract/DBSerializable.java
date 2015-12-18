package com.easyapp.mobilepad.datacontract;

import android.content.ContentValues;
import android.support.annotation.NonNull;

public interface DBSerializable {
    @NonNull ContentValues getContent();
    @NonNull String getTableName();
}
