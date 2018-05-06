package com.nikola.jakshic.dagger.data.local

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nikola.jakshic.dagger.vo.Match
import io.reactivex.Single

@Dao
interface MatchDao{

    @Query("SELECT * FROM matches WHERE matches.account_id = :id ORDER BY matches.match_id DESC")
    fun getMatches(id: Long): DataSource.Factory<Int, Match>

    @Query("SELECT COUNT(matches.account_id) FROM matches WHERE matches.account_id = :id")
    fun getMatchCount(id: Long): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMatches(list : List<Match>)

    @Query("DELETE FROM matches WHERE matches.account_id = :id")
    fun deleteMatches(id: Long)
}