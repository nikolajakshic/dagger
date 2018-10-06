package com.nikola.jakshic.dagger.repository

import androidx.paging.PagedList
import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.data.local.MatchDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Match
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class MatchBoundaryCallback(
        private val job: Job,
        private val service: OpenDotaService,
        private val dao: MatchDao,
        private val id: Long,
        private val onLoading: () -> Unit,
        private val onSuccess: () -> Unit,
        private val onError: () -> Unit) : PagedList.BoundaryCallback<Match>() {

    override fun onItemAtEndLoaded(itemAtEnd: Match) {
        onLoading()
        launch(UI, parent = job) {
            try {
                withContext(IO) {
                    val count = dao.getMatchCount(id)
                    val list = service.getMatches(id, 20, count).await()
                    list.map {
                        it.accountId = id   // response from the network doesn't contain any information
                        it           // about who played this matches, so we need to set this manually
                    }
                    dao.insertMatches(list)
                }
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }
}