package com.nikola.jakshic.dagger.profile.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchBoundaryCallback(
    private val scope: CoroutineScope,
    private val service: OpenDotaService,
    private val matchQueries: MatchQueries,
    private val dispatchers: Dispatchers,
    private val id: Long
) : PagedList.BoundaryCallback<MatchUI>() {

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

    override fun onItemAtEndLoaded(itemAtEnd: MatchUI) {
        scope.launch {
            try {
                _status.value = Status.LOADING
                withContext(dispatchers.io) {
                    val count = matchQueries.countMatches(id).executeAsOne()
                    val list = service.getMatches(id, 20, count.toInt())
                    matchQueries.transaction {
                        list.forEach { matchQueries.insert(it.mapToDb(accountId = id)) }
                    }
                }
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                retry = { onItemAtEndLoaded(itemAtEnd) }
                _status.value = Status.ERROR
            }
        }
    }
}
