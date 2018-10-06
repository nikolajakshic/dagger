package com.nikola.jakshic.dagger.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikola.jakshic.dagger.vo.Hero

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