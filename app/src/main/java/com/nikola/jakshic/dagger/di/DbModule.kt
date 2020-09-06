package com.nikola.jakshic.dagger.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nikola.jakshic.dagger.Database
import com.nikola.jakshic.dagger.common.sqldelight.DaggerSchema
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DbModule {
    @Provides
    fun providePlayerBookmarkQueries(database: Database) = database.playerBookmarkQueries

    @Provides
    fun provideMatchBookmarkQueries(database: Database) = database.matchBookmarkQueries

    @Provides
    fun providePlayerStatsQueries(database: Database) = database.playerStatsQueries

    @Provides
    fun provideMatchStatsQueries(database: Database) = database.matchStatsQueries

    @Provides
    fun providePeerQueries(database: Database) = database.peerQueries

    @Provides
    fun provideHeroQueries(database: Database) = database.heroQueries

    @Provides
    fun provideMatchQueries(database: Database) = database.matchQueries

    @Provides
    fun provideSearchHistoryQueries(database: Database) = database.searchHistoryQueries

    @Provides
    fun providePlayerQueries(database: Database) = database.playerQueries

    @Provides
    fun provideCompetitiveQueries(database: Database) = database.competitiveQueries

    @Provides
    fun provideLeaderboardQueries(database: Database) = database.leaderboardQueries

    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver {
        return AndroidSqliteDriver(
            schema = DaggerSchema,
            context = context,
            name = "dagger.db",
            callback = object : AndroidSqliteDriver.Callback(DaggerSchema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.execSQL("PRAGMA foreign_keys = ON;")
                }
            })
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver): Database {
        return Database(driver)
    }
}