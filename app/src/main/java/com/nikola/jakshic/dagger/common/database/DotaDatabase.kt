package com.nikola.jakshic.dagger.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikola.jakshic.dagger.bookmark.match.MatchBookmark
import com.nikola.jakshic.dagger.bookmark.match.MatchBookmarkDao
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmark
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkDao
import com.nikola.jakshic.dagger.competitive.Competitive
import com.nikola.jakshic.dagger.competitive.CompetitiveDao
import com.nikola.jakshic.dagger.leaderboard.Leaderboard
import com.nikola.jakshic.dagger.leaderboard.LeaderboardDao
import com.nikola.jakshic.dagger.matchstats.MatchStats
import com.nikola.jakshic.dagger.matchstats.MatchStatsDao
import com.nikola.jakshic.dagger.matchstats.PlayerStats
import com.nikola.jakshic.dagger.matchstats.PlayerStatsDao
import com.nikola.jakshic.dagger.profile.Player
import com.nikola.jakshic.dagger.profile.PlayerDao
import com.nikola.jakshic.dagger.profile.heroes.Hero
import com.nikola.jakshic.dagger.profile.heroes.HeroDao
import com.nikola.jakshic.dagger.profile.matches.Match
import com.nikola.jakshic.dagger.profile.matches.MatchDao
import com.nikola.jakshic.dagger.profile.peers.Peer
import com.nikola.jakshic.dagger.profile.peers.PeerDao
import com.nikola.jakshic.dagger.search.SearchHistory
import com.nikola.jakshic.dagger.search.SearchHistoryDao

@Database(entities = arrayOf(
    Competitive::class,
    Leaderboard::class,
    Player::class,
    SearchHistory::class,
    Match::class,
    Hero::class,
    Peer::class,
    PlayerBookmark::class,
    MatchBookmark::class,
    MatchStats::class,
    PlayerStats::class
), version = 9, exportSchema = false)
abstract class DotaDatabase : RoomDatabase() {

    abstract fun competitiveDao(): CompetitiveDao

    abstract fun leaderboardDao(): LeaderboardDao

    abstract fun playerDao(): PlayerDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun matchDao(): MatchDao

    abstract fun heroDao(): HeroDao

    abstract fun peerDao(): PeerDao

    abstract fun playerBookmarkDao(): PlayerBookmarkDao

    abstract fun matchBookmarkDao(): MatchBookmarkDao

    abstract fun matchStatsDao(): MatchStatsDao

    abstract fun playerStatsDao(): PlayerStatsDao
}