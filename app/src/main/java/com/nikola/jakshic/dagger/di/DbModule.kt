package com.nikola.jakshic.dagger.di

import android.content.Context
import androidx.room.Room
import com.nikola.jakshic.dagger.Database
import com.nikola.jakshic.dagger.common.database.DotaDatabase
import com.nikola.jakshic.dagger.common.sqldelight.CompetitiveQueries
import com.nikola.jakshic.dagger.common.sqldelight.HeroQueries
import com.nikola.jakshic.dagger.common.sqldelight.LeaderboardQueries
import com.nikola.jakshic.dagger.common.sqldelight.MatchBookmarkQueries
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import com.nikola.jakshic.dagger.common.sqldelight.MatchStatsQueries
import com.nikola.jakshic.dagger.common.sqldelight.PeerQueries
import com.nikola.jakshic.dagger.common.sqldelight.PlayerBookmarkQueries
import com.nikola.jakshic.dagger.common.sqldelight.PlayerQueries
import com.nikola.jakshic.dagger.common.sqldelight.PlayerStatsQueries
import com.nikola.jakshic.dagger.common.sqldelight.SearchHistoryQueries
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class DbModule {
    @Provides
    @Singleton
    fun provideDotaDatabase(context: Context): DotaDatabase {
        return Room.databaseBuilder(context, DotaDatabase::class.java, "dagger.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePlayerBookmarkQueries(database: Database): PlayerBookmarkQueries {
        return database.playerBookmarkQueries
    }

    @Provides
    @Singleton
    fun provideMatchBookmarkQueries(database: Database): MatchBookmarkQueries {
        return database.matchBookmarkQueries
    }

    @Provides
    @Singleton
    fun providePlayerStatsQueries(database: Database): PlayerStatsQueries {
        return database.playerStatsQueries
    }

    @Provides
    @Singleton
    fun provideMatchStatsQueries(database: Database): MatchStatsQueries {
        return database.matchStatsQueries
    }

    @Provides
    @Singleton
    fun providePeerQueries(database: Database): PeerQueries {
        return database.peerQueries
    }

    @Provides
    @Singleton
    fun provideHeroQueries(database: Database): HeroQueries {
        return database.heroQueries
    }

    @Provides
    @Singleton
    fun provideMatchQueries(database: Database): MatchQueries {
        return database.matchQueries
    }

    @Provides
    @Singleton
    fun provideSearchHistoryQueries(database: Database): SearchHistoryQueries {
        return database.searchHistoryQueries
    }

    @Provides
    @Singleton
    fun providePlayerQueries(database: Database): PlayerQueries {
        return database.playerQueries
    }

    @Provides
    @Singleton
    fun provideCompetitiveQueries(database: Database): CompetitiveQueries {
        return database.competitiveQueries
    }

    @Provides
    @Singleton
    fun provideLeaderboardQueries(database: Database): LeaderboardQueries {
        return database.leaderboardQueries
    }

    @Provides
    @Singleton
    fun provideSqlDriver(context: Context): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, context, "dagger.db")
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver): Database {
        return Database(driver)
    }
}