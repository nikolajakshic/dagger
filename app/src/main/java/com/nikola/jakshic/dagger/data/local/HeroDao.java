package com.nikola.jakshic.dagger.data.local;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nikola.jakshic.dagger.model.Hero;

import java.util.List;

@Dao
public interface HeroDao {

    @Query("SELECT * FROM heroes WHERE heroes.account_id = :id ORDER BY heroes.games DESC")
    DataSource.Factory<Integer, Hero> getHeroesByGames(long id);

    // Multiplying by 1.0 to convert from Integer to Float
    @Query("SELECT * FROM heroes WHERE heroes.account_id = :id ORDER BY ((heroes.wins*1.0/heroes.games)*100) DESC")
    DataSource.Factory<Integer, Hero> getHeroesByWinrate(long id);

    @Query("SELECT * FROM heroes WHERE heroes.account_id = :id ORDER BY heroes.wins DESC")
    DataSource.Factory<Integer, Hero> getHeroesByWins(long id);

    @Query("SELECT * FROM heroes WHERE heroes.account_id = :id ORDER BY (heroes.games-heroes.wins) DESC")
    DataSource.Factory<Integer, Hero> getHeroesByLosses(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHeroes(List<Hero> list);
}
