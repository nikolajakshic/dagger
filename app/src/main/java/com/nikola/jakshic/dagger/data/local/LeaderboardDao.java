package com.nikola.jakshic.dagger.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.dagger.model.Leaderboard;

import java.util.List;

@Dao
public interface LeaderboardDao {

    @Query("SELECT * FROM leaderboards WHERE region = :reg")
    LiveData<List<Leaderboard>> getLeaderboard(String reg);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLeaderboard(List<Leaderboard> list);

    @Query("DELETE FROM leaderboards WHERE region = :reg")
    void deleteLeaderboards(String reg);
}