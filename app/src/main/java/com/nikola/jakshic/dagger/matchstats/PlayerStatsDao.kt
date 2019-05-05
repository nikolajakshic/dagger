package com.nikola.jakshic.dagger.matchstats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PlayerStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<PlayerStats>)
}