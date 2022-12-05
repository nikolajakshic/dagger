package com.nikola.jakshic.dagger.profile.heroes

import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.HeroQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
        heroQueries.selectAllByGames(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.mapToUi() }
            .flowOn(Dispatchers.IO)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesFlowByWinrate(id: Long) =
        heroQueries.selectAllByWinrate(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.mapToUi() }
            .flowOn(Dispatchers.IO)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesFlowByWins(id: Long) =
        heroQueries.selectAllByWins(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.mapToUi() }
            .flowOn(Dispatchers.IO)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesFlowByLosses(id: Long) =
        heroQueries.selectAllByLosses(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.mapToUi() }
            .flowOn(Dispatchers.IO)

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
                heroQueries.transaction {
                    heroes.forEach { heroQueries.insert(it.mapToDb(accountId = id)) }
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}
