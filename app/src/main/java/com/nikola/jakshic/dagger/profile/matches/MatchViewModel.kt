package com.nikola.jakshic.dagger.profile.matches

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.paging.QueryDataSourceFactory
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import com.nikola.jakshic.dagger.common.sqldelight.Matches
import kotlinx.coroutines.launch

class MatchViewModel @ViewModelInject constructor(
    private val repository: MatchRepository,
    private val matchQueries: MatchQueries
) : ScopedViewModel() {

    private lateinit var response: Response

    val list: LiveData<PagedList<MatchUI>>
        get() = response.pagedList

    private val _refreshStatus = MutableLiveData<Status>()
    val refreshStatus: LiveData<Status>
        get() = _refreshStatus

    val loadMoreStatus: LiveData<Status>
        get() = response.status

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _refreshStatus.value = Status.SUCCESS }
    private val onError: () -> Unit = { _refreshStatus.value = Status.ERROR }

    // Workaround for leaky QueryDataSource, store the reference so we can release the resources.
    private var factory: QueryDataSourceFactory<Matches>? = null

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true

            val queryProvider = { limit: Long, offset: Long ->
                matchQueries.selectAll(id, limit, offset)
            }
            factory = QueryDataSourceFactory(
                queryProvider = queryProvider,
                countQuery = matchQueries.countMatches(id),
                transacter = matchQueries
            )
            response = repository.getMatchesLiveData(this, factory!!.map(Matches::mapToUi), id)
            fetchMatches(id)
        }
    }

    fun fetchMatches(id: Long) {
        launch {
            _refreshStatus.value = Status.LOADING
            repository.fetchMatches(id, onSuccess, onError)
        }
    }

    fun retry() = response.retry()

    override fun onCleared() {
        super.onCleared()
        factory?.invalidate()
    }
}