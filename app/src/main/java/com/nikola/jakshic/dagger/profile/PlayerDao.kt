package com.nikola.jakshic.dagger.profile

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players WHERE players.account_id = :id")
    fun getPlayer(id: Long): LiveData<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player)

    @Query("DELETE FROM players WHERE players.account_id = :id")
    fun deletePlayer(id: Long)
}