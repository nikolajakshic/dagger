package com.nikola.jakshic.dagger.bookmark

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nikola.jakshic.dagger.profile.Player

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmark INNER JOIN players " +
            "WHERE bookmark.account_id = players.account_id ORDER BY bookmark.count DESC")
    fun getPlayers(): LiveData<List<Player>>

    @Query("SELECT * FROM bookmark INNER JOIN players WHERE bookmark.account_id = :id")
    fun getPlayer(id: Long): LiveData<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmark WHERE bookmark.account_id = :id")
    fun removeFromBookmark(id: Long)
}