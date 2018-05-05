package com.nikola.jakshic.dagger.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.nikola.jakshic.dagger.vo.Player

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players WHERE players.account_id = :id")
    fun getPlayer(id: Long): LiveData<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player)

    @Query("DELETE FROM players WHERE players.account_id = :id")
    fun deletePlayer(id: Long)
}