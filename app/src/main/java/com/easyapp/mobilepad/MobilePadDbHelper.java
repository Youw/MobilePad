package com.easyapp.mobilepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easyapp.mobilepad.datacontract.*;

public class MobilePadDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MobilePad.db";

    public MobilePadDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Profile.TABLE_NAME + " (" +
                        Profile.ID + " integer primary key asc autoincrement," +
                        Profile.USERNAME + " text not null," +
                        Profile.EMAIL + " text not null," +
                        Profile.PASSWORD + " blob not null" +
                        ")"
                    );
        db.execSQL("create table " + Connection.TABLE_NAME + " (" +
                        Connection.PROFILE_ID + " integer references " +
                            Profile.TABLE_NAME + "," +
                        Connection.HOST + " text not null," +
                        Connection.PORT + " integer not null," +
                        "primary key (" + Connection.PROFILE_ID + ", " +
                            Connection.HOST + ", " +
                            Connection.PORT + ")" +
                        ")"
        );
        db.execSQL("create table " + Preset.TABLE_NAME + " (" +
                        Preset.PROFILE_ID + " integer references " + Profile.TABLE_NAME + "," +
                        Preset.NAME + " text not null," +
                        Preset.PRESET + " text not null," +
                        "primary key (" + Preset.PROFILE_ID + ", " + Preset.NAME + ")" +
                        ")"
        );

        // insert admin profile
        ContentValues admin_account = new ContentValues();
        admin_account.put(Profile.EMAIL, "admin@admin.com");
        byte password[] = Cryptography.encrypt("admin");
        admin_account.put(Profile.PASSWORD, password);
        admin_account.put(Profile.USERNAME, "admin");
        db.insert(Profile.TABLE_NAME, null, admin_account);

        // insert hardcoded presets
        ContentValues preset1 = new ContentValues();
        preset1.put(Preset.NAME, "Preset1");
        preset1.put(Preset.PRESET, "1");
        preset1.put(Preset.PROFILE_ID, 1);
        ContentValues preset2 = new ContentValues();
        preset2.put(Preset.NAME, "Preset2");
        preset2.put(Preset.PRESET, "2");
        preset2.put(Preset.PROFILE_ID, 1);
        db.insert(Preset.TABLE_NAME, null, preset1);
        db.insert(Preset.TABLE_NAME, null, preset2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Profile.TABLE_NAME);
        db.execSQL("drop table if exists " + Connection.TABLE_NAME);
        db.execSQL("drop table if exists " + Preset.TABLE_NAME);
        onCreate(db);
    }
}
