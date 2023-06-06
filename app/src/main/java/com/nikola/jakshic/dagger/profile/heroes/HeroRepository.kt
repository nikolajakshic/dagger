package com.nikola.jakshic.dagger.profile.heroes

import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.HeroQueries
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllByGames
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllByLosses
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllByWinrate
import com.nikola.jakshic.dagger.common.sqldelight.SelectAllByWins
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeroRepository @Inject constructor(
    private val heroQueries: HeroQueries,
    private val service: OpenDotaService,
    private val dispatchers: Dispatchers,
) {
    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesByGames(accountId: Long) =
        heroQueries.selectAllByGames(accountId)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { it.map(SelectAllByGames::mapToUi) }
            .flowOn(dispatchers.io)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesByWinrate(accountId: Long) =
        heroQueries.selectAllByWinrate(accountId)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { it.map(SelectAllByWinrate::mapToUi) }
            .flowOn(dispatchers.io)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesByWins(accountId: Long) =
        heroQueries.selectAllByWins(accountId)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { it.map(SelectAllByWins::mapToUi) }
            .flowOn(dispatchers.io)

    /**
     * Constructs the [Flow] which emits every time
     * the requested data in the database has changed
     */
    fun getHeroesByLosses(accountId: Long) =
        heroQueries.selectAllByLosses(accountId)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { it.map(SelectAllByLosses::mapToUi) }
            .flowOn(dispatchers.io)

    /**
     * Fetches the heroes from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [Flow]
     * returned by [getHeroesByGames], [getHeroesByWins],
     * [getHeroesByLosses] and [getHeroesByWinrate] are notified.
     */
    suspend fun fetchHeroes(accountId: Long) {
        val heroes = service.getHeroes(accountId)
        withContext(dispatchers.io) {
            heroQueries.transaction {
                heroes.forEach { heroQueries.insert(it.mapToDb(accountId = accountId)) }
            }
        }
    }
}
