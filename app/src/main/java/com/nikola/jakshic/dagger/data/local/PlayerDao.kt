package com.nikola.jakshic.dagger.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
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