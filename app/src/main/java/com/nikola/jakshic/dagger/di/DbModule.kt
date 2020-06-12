package com.nikola.jakshic.dagger.di

import android.content.Context
import androidx.room.Room
import com.nikola.jakshic.dagger.Database
import com.nikola.jakshic.dagger.common.database.DotaDatabase
import com.nikola.jakshic.dagger.common.sqldelight.CompetitiveQueries
import com.nikola.jakshic.dagger.common.sqldelight.LeaderboardQueries
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class DbModule {

    @Provides
    fun providePlayerDao(db: DotaDatabase) = db.playerDao()

    @Provides
    fun provideSearchHistoryDao(db: DotaDatabase) = db.searchHistoryDao()

    @Provides
    fun providePeerDao(db: DotaDatabase) = db.peerDao()

    @Provides
    fun provideHeroDao(db: DotaDatabase) = db.heroDao()

    @Provides
    fun provideMatchDao(db: DotaDatabase) = db.matchDao()

    @Provides
    fun providePlayerBookmarkDao(db: DotaDatabase) = db.playerBookmarkDao()

    @Provides
    fun provideMatchBookmarkDao(db: DotaDatabase) = db.matchBookmarkDao()

    @Provides
    fun provideMatchStatsDao(db: DotaDatabase) = db.matchStatsDao()

    @Provides
    fun providePlayerStatsDao(db: DotaDatabase) = db.playerStatsDao()

    @Provides
    @Singleton
    fun provideDotaDatabase(context: Context): DotaDatabase {
        return Room.databaseBuilder(context, DotaDatabase::class.java, "dagger.db")
            .fallbackToDestructiveMigration()
            .build()
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