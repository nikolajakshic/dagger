package com.nikola.jakshic.dagger.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.nikola.jakshic.dagger.model.Competitive;
import com.nikola.jakshic.dagger.model.Hero;
import com.nikola.jakshic.dagger.model.Leaderboard;
import com.nikola.jakshic.dagger.model.Peer;
import com.nikola.jakshic.dagger.model.Player;
import com.nikola.jakshic.dagger.model.SearchHistory;
import com.nikola.jakshic.dagger.model.match.Match;

@Database(
        entities = {
                Player.class,
                SearchHistory.class,
                Competitive.class,
                Match.class,
                Peer.class,
                Hero.class,
                Leaderboard.class},
        version = 1,
        exportSchema = false)

public abstract class DotaDatabase extends RoomDatabase {

    public abstract PlayerDao playerDao();

    public abstract SearchHistoryDao searchHistoryDao();

    public abstract CompetitiveDao competitiveDao();

    public abstract PeerDao peerDao();

    public abstract HeroDao heroDao();

    public abstract MatchDao matchDao();

    public abstract LeaderboardDao leaderboardDao();
}