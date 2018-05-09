package com.nikola.jakshic.dagger.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nikola.jakshic.dagger.vo.MatchStats
import com.nikola.jakshic.dagger.vo.Stats

@Dao
interface MatchStatsDao {

    @Query("SELECT * FROM match_stats WHERE match_id = :id")
    fun getMatchStats(id: Long) : LiveData<Stats>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MatchStats)
}