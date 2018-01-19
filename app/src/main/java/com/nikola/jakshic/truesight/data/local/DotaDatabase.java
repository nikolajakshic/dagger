package com.nikola.jakshic.truesight.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.nikola.jakshic.truesight.model.Player;

@Database(entities = Player.class, version = 1, exportSchema = false)
public abstract class DotaDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();
}