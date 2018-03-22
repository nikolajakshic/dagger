package com.nikola.jakshic.dagger.data.local;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.dagger.model.Peer;

import java.util.List;

@Dao
public interface PeerDao {

    @Query("SELECT * FROM peers WHERE peers.account_id = :id ORDER BY games DESC")
    DataSource.Factory<Integer, Peer> getByGames(long id);

    // Multiplying by 1.0 to convert from Integer to Float
    @Query("SELECT * FROM peers WHERE peers.account_id = :id ORDER BY ((peers.wins*1.0/peers.games)*100) DESC")
    DataSource.Factory<Integer, Peer> getByWinrate(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPeers(List<Peer> peers);
}