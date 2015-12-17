package com.easyapp.mobilepad;

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
                        Profile.NAME + " text not null," +
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
        db.execSQL("insert into " + Profile.TABLE_NAME + "(" + Profile.NAME + ") " +
                        "values('admin@admin.com')"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Profile.TABLE_NAME);
        db.execSQL("drop table if exists " + Connection.TABLE_NAME);
        db.execSQL("drop table if exists " + Preset.TABLE_NAME);
        onCreate(db);
    }
}
