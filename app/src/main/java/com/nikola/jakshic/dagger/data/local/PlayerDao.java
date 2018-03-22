package com.nikola.jakshic.dagger.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.dagger.model.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM players WHERE players.id = :id")
    LiveData<Player> getPlayer(long id);

    @Query("SELECT * FROM players ORDER BY players.count DESC")
    LiveData<List<Player>> getPlayers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayer(Player player);

    @Query("DELETE FROM players WHERE players.id = :id")
    void deletePlayer(long id);
}