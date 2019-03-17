package com.nikola.jakshic.dagger.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.data.local.MatchDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Match
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchBoundaryCallback(
        private val scope: CoroutineScope,
        private val service: OpenDotaService,
        private val dao: MatchDao,
        private val id: Long) : PagedList.BoundaryCallback<Match>() {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var retry: (() -> Any)? = null
        get() {
            val tmp = field
            field = null
            return tmp
        }

    fun retry() {
        retry?.invoke()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Match) {
        scope.launch {
            try {
                _status.value = Status.LOADING
                withContext(Dispatchers.IO) {
                    val count = dao.getMatchCount(id)
                    val list = service.getMatches(id, 20, count)
                    list.map {
                        it.accountId = id   // response from the network doesn't contain any information
                        it           // about who played this matches, so we need to set this manually
                    }
                    dao.insertMatches(list)
                }
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                retry = { onItemAtEndLoaded(itemAtEnd) }
                _status.value = Status.ERROR
            }
        }
    }
}