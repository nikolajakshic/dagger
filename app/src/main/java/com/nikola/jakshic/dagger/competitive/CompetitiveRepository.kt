package com.nikola.jakshic.dagger.competitive

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompetitiveRepository @Inject constructor(
        private val dao: CompetitiveDao,
        private val service: OpenDotaService) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getCompetitiveLiveData(): LiveData<PagedList<Competitive>> {
        val factory = dao.getMatches()
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
                dao.insertMatches(matches)
            }
            onSuccess()
        } catch (e: Exception) {
            onError()
        }
    }
}