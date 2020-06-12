package com.nikola.jakshic.dagger.bookmark.player

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikola.jakshic.dagger.profile.PlayerJson

@Dao
interface PlayerBookmarkDao {

    @Query("SELECT * FROM bookmark INNER JOIN players " +
        "WHERE bookmark.account_id = players.account_id ORDER BY bookmark.count DESC")
    fun getPlayers(): LiveData<List<PlayerJson>>

    @Query("SELECT * FROM bookmark INNER JOIN players WHERE bookmark.account_id = :id")
    fun getPlayer(id: Long): LiveData<PlayerJson>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToBookmark(bookmark: PlayerBookmark)

    @Query("DELETE FROM bookmark WHERE bookmark.account_id = :id")
    fun removeFromBookmark(id: Long)
}