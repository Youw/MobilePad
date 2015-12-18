package com.easyapp.mobilepad;

import com.easyapp.mobilepad.datacontract.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBConnection implements DBConnection {

    private static class SQLiteDBConnectionHolder {
        public static final SQLiteDBConnection INSTANCE = new SQLiteDBConnection();
    }

    private SQLiteDBConnection(){
        mDBHelper = new MobilePadDbHelper(mContext);
    }

    public static Context mContext = null;
    private MobilePadDbHelper mDBHelper = null;

    public static SQLiteDBConnection getInstance(Context context) {
        if (mContext == null) mContext = context;
        return SQLiteDBConnectionHolder.INSTANCE;
    }

    @Override
    public Profile getProfile(@NonNull String login) {
        try (SQLiteDatabase db = mDBHelper.getReadableDatabase()) {
            String[] projection = {
                    Profile.ID,
                    Profile.PASSWORD
            };
            String[] where = { login };

            try (Cursor c = db.query(Profile.TABLE_NAME,
                    projection, Profile.NAME + "=?", where, null, null, null)) {
                if (c.moveToFirst()) {
                    int id = c.getInt(0);
                    byte[] password = c.getBlob(1);
                    return new Profile(id, login, password);
                }
            }
        }
        return null;
    }

    @Override
    public List<Connection> getConnections(int profileId) {
        ArrayList<Connection> result = new ArrayList<>();
        try (SQLiteDatabase db = mDBHelper.getReadableDatabase()){
            String[] projection = {
                    Connection.HOST,
                    Connection.PORT
            };
            String[] where = { String.valueOf(profileId) };

            try (Cursor c = db.query(Connection.TABLE_NAME,
                    projection, Connection.PROFILE_ID + "=?", where, null, null, null)) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    String host = c.getString(0);
                    int port = c.getInt(1);
                    result.add(new Connection(host, port));
                    c.moveToNext();
                }
            }
        }
        return result;
    }

    @Override
    public List<Preset> getPresets(int profileId) {
        ArrayList<Preset> result = new ArrayList<>();
        try(SQLiteDatabase db = mDBHelper.getReadableDatabase()) {
            String[] projection = {
                    Preset.NAME,
                    Preset.PRESET
            };
            String[] where = {String.valueOf(profileId)};

            try (Cursor c = db.query(Preset.TABLE_NAME,
                    projection, Preset.PROFILE_ID + "=?", where, null, null, null)) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    String name = c.getString(0);
                    String preset = c.getString(1);
                    result.add(new Preset(name, preset));
                    c.moveToNext();
                }
            }
        }
        return result;
    }

    @Override
    public boolean update(@NonNull DBSerializable obj) {
        long result = -1;
        try(SQLiteDatabase db = mDBHelper.getWritableDatabase()) {
            result = db.insert(obj.getTableName(), null, obj.getContent());
        }
        return result != -1;
    }
}
