package com.nikola.jakshic.dagger.leaderboard

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LeaderboardDao {

    @Query("SELECT * FROM leaderboards WHERE region = :reg")
    fun getLeaderboard(reg: String): LiveData<List<Leaderboard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLeaderboard(list: List<Leaderboard>)

    @Query("DELETE FROM leaderboards WHERE region = :reg")
    fun deleteLeaderboards(reg: String)
}