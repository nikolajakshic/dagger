package com.nikola.jakshic.dagger.data.local;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.dagger.model.Competitive;

import java.util.List;

@Dao
public interface CompetitiveDao {

    @Query("SELECT * FROM competitive ORDER BY start_time+duration DESC")
    DataSource.Factory<Integer, Competitive> getMatches();

    @Query("SELECT COUNT(match_id) FROM competitive")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatches(List<Competitive> matches);
}