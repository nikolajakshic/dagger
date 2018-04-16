package com.nikola.jakshic.dagger.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.dagger.model.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao {

    @Query("SELECT * FROM searchhistory ORDER BY id DESC")
    List<SearchHistory> getAllQueries();

    @Query("SELECT * FROM searchhistory WHERE searchhistory.`query` LIKE :query || '%' ORDER BY id DESC")
    List<SearchHistory> getQuery(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuery(SearchHistory query);

    @Query("DELETE FROM searchhistory")
    void deleteHistory();
}