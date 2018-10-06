package com.nikola.jakshic.dagger.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.nikola.jakshic.dagger.vo.PlayerStats

@Dao
interface PlayerStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<PlayerStats>)
}