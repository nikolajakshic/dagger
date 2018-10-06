package com.nikola.jakshic.dagger.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.data.local.CompetitiveDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Competitive
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
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
     *
     * @param onSuccess called on main thread
     * @param onError called on main thread
     */
    fun fetchCompetitive(job: Job, onSuccess: () -> Unit, onError: () -> Unit) {
        launch(UI, parent = job) {
            try {
                val matches = service.getCompetitiveMatches().await()
                withContext(IO) { dao.insertMatches(matches) }
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }
}