package com.nikola.jakshic.dagger.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.nikola.jakshic.dagger.model.Competitive;
import com.nikola.jakshic.dagger.model.Peer;
import com.nikola.jakshic.dagger.model.Player;
import com.nikola.jakshic.dagger.model.SearchHistory;

@Database(entities = {Player.class, SearchHistory.class, Competitive.class, Peer.class}, version = 1, exportSchema = false)
public abstract class DotaDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();

    public abstract SearchHistoryDao searchHistoryDao();

    public abstract CompetitiveDao competitiveDao();

    public abstract PeerDao peerDao();
}