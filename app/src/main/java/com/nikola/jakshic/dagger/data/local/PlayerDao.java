package com.nikola.jakshic.dagger.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.dagger.model.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM player WHERE player.id = :id")
    LiveData<Player> getPlayer(long id);

    @Query("SELECT * FROM player ORDER BY player.count DESC")
    LiveData<List<Player>> getPlayers();

    @Insert
    void insertPlayer(Player player);

    @Query("DELETE FROM player WHERE player.id = :id")
    void deletePlayer(long id);
}