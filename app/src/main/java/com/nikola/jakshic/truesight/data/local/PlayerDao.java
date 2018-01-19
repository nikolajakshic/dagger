package com.nikola.jakshic.truesight.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.truesight.model.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM player WHERE player.id = :id")
    LiveData<List<Player>> getPlayer(long id);

    @Query("SELECT * FROM player")
    LiveData<List<Player>> getPlayers();

    @Insert
    void insertPlayer(Player player);

    @Delete
    void deletePlayer(Player player);
}