package com.nikola.jakshic.dagger.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nikola.jakshic.dagger.model.Player

@Dao
interface PlayerDao {

    @Query("SELECT * FROM players WHERE players.id = :id")
    fun getPlayer(id: Long): LiveData<Player>

    @Query("SELECT * FROM players ORDER BY players.count DESC")
    fun getPlayers(): LiveData<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player)

    @Query("DELETE FROM players WHERE players.id = :id")
    fun deletePlayer(id: Long)
}