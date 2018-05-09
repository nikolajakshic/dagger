package com.nikola.jakshic.dagger.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import com.nikola.jakshic.dagger.vo.PlayerStats

@Dao
interface PlayerStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<PlayerStats>)
}