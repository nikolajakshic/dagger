package com.nikola.jakshic.dagger.profile.matches

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MatchDao {

    @Query("SELECT * FROM matches WHERE matches.account_id = :id ORDER BY matches.match_id DESC")
    fun getMatches(id: Long): DataSource.Factory<Int, Match>

    @Query("SELECT COUNT(matches.account_id) FROM matches WHERE matches.account_id = :id")
    fun getMatchCount(id: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatches(list: List<Match>)

    @Query("DELETE FROM matches WHERE matches.account_id = :id")
    fun deleteMatches(id: Long)
}