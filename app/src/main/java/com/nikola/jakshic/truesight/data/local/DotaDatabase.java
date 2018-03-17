package com.nikola.jakshic.truesight.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.nikola.jakshic.truesight.model.Competitive;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.model.SearchHistory;

@Database(entities = {Player.class, SearchHistory.class, Competitive.class}, version = 1, exportSchema = false)
public abstract class DotaDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();

    public abstract SearchHistoryDao searchHistoryDao();

    public abstract CompetitiveDao competitiveDao();
}