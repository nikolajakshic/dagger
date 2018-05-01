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
    @Singleton
    fun providePlayerDao(db: DotaDatabase) = db.playerDao()

    @Provides
    @Singleton
    fun provideCompetitiveDao(db: DotaDatabase) = db.competitiveDao()

    @Provides
    @Singleton
    fun provideSearchHistoryDao(db: DotaDatabase) = db.searchHistoryDao()

    @Provides
    @Singleton
    fun providePeerDao(db: DotaDatabase) = db.peerDao()

    @Provides
    @Singleton
    fun provideHeroDao(db: DotaDatabase) = db.heroDao()

    @Provides
    @Singleton
    fun provideMatchDao(db: DotaDatabase) = db.matchDao()

    @Provides
    @Singleton
    fun provideLeaderboardDao(db: DotaDatabase) = db.leaderboardDao()

    @Provides
    @Singleton
    fun provideBookmark(db: DotaDatabase) = db.bookmarkDao()

    @Provides
    @Singleton
    fun provideDotaDatabase(context: Context): DotaDatabase {
        return Room.databaseBuilder(context, DotaDatabase::class.java, "dagger.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}