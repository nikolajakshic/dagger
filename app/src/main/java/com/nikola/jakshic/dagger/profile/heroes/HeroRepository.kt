package com.nikola.jakshic.dagger.profile.heroes

import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.HeroQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroRepository @Inject constructor(
    private val heroQueries: HeroQueries,
    private val service: OpenDotaService
) {

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesFlowByGames(id: Long) =
        heroQueries.selectAllByGames(id, ::mapToUi)
            .asFlow()
            .mapToList(Dispatchers.IO)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesFlowByWinrate(id: Long) =
        heroQueries.selectAllByWinrate(id, ::mapToUi)
            .asFlow()
            .mapToList(Dispatchers.IO)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesFlowByWins(id: Long) =
        heroQueries.selectAllByWins(id, ::mapToUi)
            .asFlow()
            .mapToList(Dispatchers.IO)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesFlowByLosses(id: Long) =
        heroQueries.selectAllByLosses(id, ::mapToUi)
            .asFlow()
            .mapToList(Dispatchers.IO)

    /**
     * Fetches the heroes from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [Flow]
     * returned by [getHeroesFlowByGames], [getHeroesFlowByWins],
     * [getHeroesFlowByLosses] and [getHeroesFlowByWinrate] are notified.
     */
    suspend fun fetchHeroes(id: Long, onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                val heroes = service.getHeroes(id)
                heroes.map {
                    it.accountId = id // response from the network doesn't contain any information
                    it // about who played this heroes, so we need to set this manually
                }
                heroQueries.transaction {
                    heroes.forEach { heroQueries.insert(it.mapToDb()) }
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}