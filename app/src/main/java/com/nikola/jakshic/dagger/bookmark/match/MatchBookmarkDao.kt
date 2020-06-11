package com.nikola.jakshic.dagger.bookmark.match

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MatchBookmarkDao {

    @Query("SELECT * FROM bookmark_match INNER JOIN match_stats " +
        "WHERE bookmark_match.match_id = match_stats.match_id ORDER BY bookmark_match.id DESC")
    fun getMatches(): LiveData<List<MatchesNotes>>

    @Query("SELECT COUNT(*) FROM bookmark_match WHERE match_id = :matchId")
    fun isMatchBookmarked(matchId: Long): LiveData<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToBookmark(bookmark: MatchBookmark)

    @Query("UPDATE bookmark_match SET note = :note WHERE match_id = :matchId")
    fun updateNote(note: String?, matchId: Long)

    @Query("DELETE FROM bookmark_match WHERE bookmark_match.match_id = :matchId")
    fun removeFromBookmark(matchId: Long)
}