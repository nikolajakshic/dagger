package com.nikola.jakshic.dagger.competitive

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.paging.QueryDataSourceFactory
import com.nikola.jakshic.dagger.common.sqldelight.Competitive
import com.nikola.jakshic.dagger.common.sqldelight.CompetitiveQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompetitiveRepository @Inject constructor(
    private val competitiveQueries: CompetitiveQueries,
    private val service: OpenDotaService
) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getCompetitiveLiveData(): LiveData<PagedList<Competitive>> {
        val factory = QueryDataSourceFactory(
            queryProvider = competitiveQueries::getMatches,
            countQuery = competitiveQueries.countMatches(),
            transacter = competitiveQueries
        )
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(80)
            .setPageSize(40)
            .setPrefetchDistance(15)
            .setEnablePlaceholders(false)
            .build()
        return LivePagedListBuilder(factory, config).build()
    }

    /**
     * Fetches the matches from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getCompetitiveLiveData] are notified.
     */
    suspend fun fetchCompetitive(onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                val matches = service.getCompetitiveMatches()
                    .map {
                        Competitive(
                            match_id = it.matchId,
                            start_time = it.startTime,
                            duration = it.duration.toLong(),
                            radiant_name = it.radiantName,
                            dire_name = it.direName,
                            league_name = it.leagueName,
                            radiant_score = it.radiantScore.toLong(),
                            dire_score = it.direScore.toLong(),
                            radiant_win = it.isRadiantWin
                        )
                    }
                competitiveQueries.transaction {
                    matches.forEach(competitiveQueries::insert)
                }
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}