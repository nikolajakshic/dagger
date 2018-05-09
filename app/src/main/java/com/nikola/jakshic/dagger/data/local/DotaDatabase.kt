package com.nikola.jakshic.dagger.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.nikola.jakshic.dagger.vo.*
import com.nikola.jakshic.dagger.vo.Match

@Database(entities = arrayOf(
        Competitive::class,
        Leaderboard::class,
        Player::class,
        SearchHistory::class,
        Match::class,
        Hero::class,
        Peer::class,
        Bookmark::class,
        MatchStats::class,
        PlayerStats::class
), version = 5, exportSchema = false)
abstract class DotaDatabase : RoomDatabase() {

    abstract fun competitiveDao(): CompetitiveDao

    abstract fun leaderboardDao(): LeaderboardDao

    abstract fun playerDao(): PlayerDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun matchDao(): MatchDao

    abstract fun heroDao(): HeroDao

    abstract fun peerDao(): PeerDao

    abstract fun bookmarkDao(): BookmarkDao

    abstract fun matchStatsDao() : MatchStatsDao

    abstract fun playerStatsDao() : PlayerStatsDao
}