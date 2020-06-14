package com.nikola.jakshic.dagger.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikola.jakshic.dagger.bookmark.match.MatchBookmark
import com.nikola.jakshic.dagger.bookmark.match.MatchBookmarkDao
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmark
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkDao
import com.nikola.jakshic.dagger.competitive.CompetitiveJson
import com.nikola.jakshic.dagger.leaderboard.LeaderboardJson
import com.nikola.jakshic.dagger.matchstats.MatchStats
import com.nikola.jakshic.dagger.matchstats.MatchStatsDao
import com.nikola.jakshic.dagger.matchstats.PlayerStats
import com.nikola.jakshic.dagger.matchstats.PlayerStatsDao
import com.nikola.jakshic.dagger.profile.PlayerJson
import com.nikola.jakshic.dagger.profile.heroes.HeroJson
import com.nikola.jakshic.dagger.profile.matches.MatchJson
import com.nikola.jakshic.dagger.profile.peers.Peer
import com.nikola.jakshic.dagger.profile.peers.PeerDao
import com.nikola.jakshic.dagger.search.SearchHistory

@Database(entities = arrayOf(
    CompetitiveJson::class,
    LeaderboardJson::class,
    PlayerJson::class,
    SearchHistory::class,
    MatchJson::class,
    HeroJson::class,
    Peer::class,
    PlayerBookmark::class,
    MatchBookmark::class,
    MatchStats::class,
    PlayerStats::class
), version = 9, exportSchema = false)
abstract class DotaDatabase : RoomDatabase() {

    abstract fun peerDao(): PeerDao

    abstract fun playerBookmarkDao(): PlayerBookmarkDao

    abstract fun matchBookmarkDao(): MatchBookmarkDao

    abstract fun matchStatsDao(): MatchStatsDao

    abstract fun playerStatsDao(): PlayerStatsDao
}