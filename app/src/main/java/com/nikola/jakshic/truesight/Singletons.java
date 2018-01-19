package com.nikola.jakshic.truesight;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.nikola.jakshic.truesight.data.local.DotaDatabase;

public final class Singletons {

    private static DotaDatabase db;

    public synchronized static DotaDatabase getDb(Context context) {
        if (db == null)
            db = Room.databaseBuilder(context, DotaDatabase.class, "dotka")
                    .allowMainThreadQueries().build();
        return db;
    }
}