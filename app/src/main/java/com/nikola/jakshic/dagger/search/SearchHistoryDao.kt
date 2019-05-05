package com.nikola.jakshic.dagger.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY id DESC")
    fun getAllQueries(): List<SearchHistory>

    @Query("SELECT * FROM search_history WHERE search_history.`query` LIKE :query || '%' ORDER BY id DESC")
    fun getQuery(query: String): List<SearchHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuery(query: SearchHistory)

    @Query("DELETE FROM search_history")
    fun deleteHistory()
}