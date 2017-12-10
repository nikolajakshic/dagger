package com.nikola.jakshic.truesight.data.local;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nikola.jakshic.truesight.data.local.DotaContract.DotaSubscriber;

public class DotaDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "opendotalocal.db";
    private static final int DATABASE_VERSION = 1;

    public DotaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DotaSubscriber.TABLE_NAME + "("
                + DotaSubscriber._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DotaSubscriber.COLUMN_AVATAR_URL + " TEXT NOT NULL, "
                + DotaSubscriber.COLUMN_PLAYER_NAME + " TEXT NOT NULL, "
                + DotaSubscriber.COLUMN_ACC_ID + " INTEGER NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DotaSubscriber.TABLE_NAME + ";");
    }
}
