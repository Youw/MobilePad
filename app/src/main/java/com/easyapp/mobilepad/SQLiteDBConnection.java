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

    static private final String[] PROFILE_PROJECTION = {
            Profile.ID,
            Profile.USERNAME,
            Profile.EMAIL,
            Profile.PASSWORD
    };

    private Profile profileFromCursor(Cursor c) {
        int id = c.getInt(0);
        String username = c.getString(1);
        String email = c.getString(2);
        byte[] password = c.getBlob(3);
        return new Profile(id, username, email, password);
    }

    @Override
    public Profile getProfileByUsername(@NonNull String username) {
        try (SQLiteDatabase db = mDBHelper.getReadableDatabase()) {
            String[] where = { username.toLowerCase() };

            try (Cursor c = db.query(Profile.TABLE_NAME, PROFILE_PROJECTION, Profile.USERNAME + "=?", where, null, null, null)) {
                if (c.moveToFirst()) {
                    return profileFromCursor(c);
                }
            }
        }
        return null;
    }

    @Override
    public Profile getProfileByEmail(@NonNull String email) {
        try (SQLiteDatabase db = mDBHelper.getReadableDatabase()) {
            String[] where = { email.toLowerCase() };

            try (Cursor c = db.query(Profile.TABLE_NAME, PROFILE_PROJECTION, Profile.EMAIL + "=?", where, null, null, null)) {
                if (c.moveToFirst()) {
                    return profileFromCursor(c);
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
    public boolean createProfile(String username, String email, byte[] passwordHash) {
        return insert(new Profile(-1, username, email, passwordHash));
    }

    @Override
    public boolean insert(@NonNull DBSerializable obj) {
        long result;
        try(SQLiteDatabase db = mDBHelper.getWritableDatabase()) {
            result = db.insert(obj.getTableName(), null, obj.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        }
        return result != -1;
    }
}
