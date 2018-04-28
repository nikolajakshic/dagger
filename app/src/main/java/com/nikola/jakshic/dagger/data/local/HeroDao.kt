package com.nikola.jakshic.dagger.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nikola.jakshic.dagger.model.Hero

@Dao
interface HeroDao {

    @Query("SELECT * FROM heroes WHERE heroes.account_id = :id ORDER BY heroes.games DESC")
    fun getHeroesByGames(id: Long): LiveData<List<Hero>>

    // Multiplying by 1.0 to convert from Integer to Float
    @Query("SELECT * FROM heroes WHERE heroes.account_id = :id ORDER BY ((heroes.wins*1.0/heroes.games)*100) DESC")
    fun getHeroesByWinrate(id: Long): LiveData<List<Hero>>

    @Query("SELECT * FROM heroes WHERE heroes.account_id = :id ORDER BY heroes.wins DESC")
    fun getHeroesByWins(id: Long): LiveData<List<Hero>>

    @Query("SELECT * FROM heroes WHERE heroes.account_id = :id ORDER BY (heroes.games-heroes.wins) DESC")
    fun getHeroesByLosses(id: Long): LiveData<List<Hero>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeroes(list: List<Hero>)
}