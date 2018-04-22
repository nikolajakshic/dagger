package com.nikola.jakshic.dagger.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nikola.jakshic.dagger.model.SearchHistory

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM searchhistory ORDER BY id DESC")
    fun getAllQueries(): List<SearchHistory>

    @Query("SELECT * FROM searchhistory WHERE searchhistory.`query` LIKE :query || '%' ORDER BY id DESC")
    fun getQuery(query: String): List<SearchHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuery(query: SearchHistory)

    @Query("DELETE FROM searchhistory")
    fun deleteHistory()
}