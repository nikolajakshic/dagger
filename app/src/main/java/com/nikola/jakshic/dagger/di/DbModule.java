package com.nikola.jakshic.dagger.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.nikola.jakshic.dagger.data.local.CompetitiveDao;
import com.nikola.jakshic.dagger.data.local.DotaDatabase;
import com.nikola.jakshic.dagger.data.local.HeroDao;
import com.nikola.jakshic.dagger.data.local.PeerDao;
import com.nikola.jakshic.dagger.data.local.PlayerDao;
import com.nikola.jakshic.dagger.data.local.SearchHistoryDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = NetworkModule.class)
public class DbModule {

    @Provides
    @Singleton
    PlayerDao providePlayerDao(DotaDatabase db) {
        return db.playerDao();
    }

    @Provides
    @Singleton
    CompetitiveDao provideCompetitiveDao(DotaDatabase db) {
        return db.competitiveDao();
    }

    @Provides
    @Singleton
    SearchHistoryDao provideSearchHistoryDao(DotaDatabase db) {
        return db.searchHistoryDao();
    }

    @Provides
    @Singleton
    PeerDao providePeerDao(DotaDatabase db) {
        return db.peerDao();
    }

    @Provides
    @Singleton
    HeroDao provideHeroDao(DotaDatabase db) {
        return db.heroDao();
    }

    @Provides
    @Singleton
    DotaDatabase provideDotaDatabase(Context context) {
        return Room.databaseBuilder(context, DotaDatabase.class, "dagger.db").build();
    }
}