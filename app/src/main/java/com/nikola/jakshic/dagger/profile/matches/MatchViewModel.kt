package com.nikola.jakshic.dagger.profile.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.paging.QueryDataSourceFactory
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import com.nikola.jakshic.dagger.common.sqldelight.match.SelectAll
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository,
    private val matchQueries: MatchQueries
) : ViewModel() {
    private lateinit var response: Response

    val list: LiveData<PagedList<MatchUI>>
        get() = response.pagedList

    private val _refreshStatus = MutableLiveData<Status>()
    val refreshStatus: LiveData<Status>
        get() = _refreshStatus

    val loadMoreStatus: LiveData<Status>
        get() = response.status

    private var initialFetch = false

    // Workaround for leaky QueryDataSource, store the reference so we can release the resources.
    private var factory: QueryDataSourceFactory<SelectAll>? = null

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
            response =
                repository.getMatchesLiveData(viewModelScope, factory!!.map(SelectAll::mapToUi), id)
            fetchMatches(id)
        }
    }

    fun fetchMatches(id: Long) {
        viewModelScope.launch {
            try {
                _refreshStatus.value = Status.LOADING
                repository.fetchMatches(id)
                _refreshStatus.value = Status.SUCCESS
            } catch (e: Exception) {
                Timber.e(e)
                _refreshStatus.value = Status.ERROR
            }
        }
    }

    fun retry() = response.retry()

    override fun onCleared() {
        super.onCleared()
        factory?.invalidate()
    }
}
