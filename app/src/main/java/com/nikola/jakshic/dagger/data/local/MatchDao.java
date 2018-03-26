package com.nikola.jakshic.dagger.data.local;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.dagger.model.match.Match;

import java.util.List;

@Dao
public interface MatchDao {

    @Query("SELECT * FROM matches WHERE matches.account_id = :id ORDER BY matches.match_id DESC")
    DataSource.Factory<Integer, Match> getMatches(long id);

    @Query("SELECT COUNT(matches.account_id) FROM matches WHERE matches.account_id = :id")
    long getMatchCount(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMatches(List<Match> list);

    @Query("DELETE FROM matches WHERE matches.account_id = :id")
    void deleteMatches(long id);
}
