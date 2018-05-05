package com.nikola.jakshic.dagger.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nikola.jakshic.dagger.vo.Leaderboard

@Dao
interface LeaderboardDao {

    @Query("SELECT * FROM leaderboards WHERE region = :reg")
    fun getLeaderboard(reg: String): LiveData<List<Leaderboard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLeaderboard(list: List<Leaderboard>)

    @Query("DELETE FROM leaderboards WHERE region = :reg")
    fun deleteLeaderboards(reg: String)
}