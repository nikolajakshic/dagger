package com.nikola.jakshic.dagger.matchstats

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MatchStatsDao {

    @Query("SELECT * FROM match_stats WHERE match_id = :id")
    fun getMatchStats(id: Long) : LiveData<Stats>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MatchStats)
}