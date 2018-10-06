package com.nikola.jakshic.dagger.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikola.jakshic.dagger.vo.Peer

@Dao
interface PeerDao {

    @Query("SELECT * FROM peers WHERE peers.account_id = :id ORDER BY games DESC")
    fun getByGames(id: Long):  LiveData<List<Peer>>

    // Multiplying by 1.0 to convert from Integer to Float
    @Query("SELECT * FROM peers WHERE peers.account_id = :id ORDER BY ((peers.wins*1.0/peers.games)*100) DESC")
    fun getByWinrate(id: Long): LiveData<List<Peer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPeers(peers: List<Peer>)
}