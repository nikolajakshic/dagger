package com.nikola.jakshic.dagger.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikola.jakshic.dagger.vo.MatchStats
import com.nikola.jakshic.dagger.vo.Stats

@Dao
interface MatchStatsDao {

    @Query("SELECT * FROM match_stats WHERE match_id = :id")
    fun getMatchStats(id: Long) : LiveData<Stats>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MatchStats)
}