package com.nikola.jakshic.dagger.di

import android.arch.persistence.room.Room
import android.content.Context
import com.nikola.jakshic.dagger.data.local.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class DbModule {

    @Provides
    fun providePlayerDao(db: DotaDatabase) = db.playerDao()

    @Provides
    fun provideCompetitiveDao(db: DotaDatabase) = db.competitiveDao()

    @Provides
    fun provideSearchHistoryDao(db: DotaDatabase) = db.searchHistoryDao()

    @Provides
    fun providePeerDao(db: DotaDatabase) = db.peerDao()

    @Provides
    fun provideHeroDao(db: DotaDatabase) = db.heroDao()

    @Provides
    fun provideMatchDao(db: DotaDatabase) = db.matchDao()

    @Provides
    fun provideLeaderboardDao(db: DotaDatabase) = db.leaderboardDao()

    @Provides
    fun provideBookmarkDao(db: DotaDatabase) = db.bookmarkDao()

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
}