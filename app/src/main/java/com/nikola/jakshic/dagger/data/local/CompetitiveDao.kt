package com.nikola.jakshic.dagger.data.local

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nikola.jakshic.dagger.model.Competitive

@Dao
interface CompetitiveDao{

    @Query("SELECT * FROM competitive ORDER BY start_time+duration DESC")
    fun getMatches(): DataSource.Factory<Int, Competitive>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatches(list: List<Competitive>)

    @Query("SELECT COUNT(match_id) FROM competitive")
    fun getCount(): Int
}