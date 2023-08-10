package com.nikola.jakshic.dagger.profile.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import app.cash.sqldelight.paging3.QueryPagingSource
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.paging.QueryDataSourceFactory
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import com.nikola.jakshic.dagger.common.sqldelight.match.SelectAll
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repository: MatchRepository,
    private val matchQueries: MatchQueries,
    private val matchQueriesExt: MatchQueriesExt,
    private val dispatchers: Dispatchers,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val accountId = MatchFragment.getAccountId(savedStateHandle)

    private var response: Response

    val list: LiveData<PagedList<MatchUI>>
        get() = response.pagedList

    private val _refreshStatus = MutableLiveData<Status>()
    val refreshStatus: LiveData<Status>
        get() = _refreshStatus

    val loadMoreStatus: LiveData<Status>
        get() = response.status

    val data = Pager(
        config = PagingConfig(
            pageSize = 40,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 40,
        ),
        pagingSourceFactory = {
            QueryPagingSource(
                transacter = matchQueries,
                context = dispatchers.io,
                pageBoundariesProvider = { anchor: Long?, limit: Long ->
                    matchQueriesExt.pageBoundaries(
                        limit,
                        anchor,
                        accountId,
                    )
                },
                queryProvider = { beginInclusive: Long, endExclusive: Long? ->
                    matchQueries.keyedQuery(
                        accountId,
                        beginInclusive,
                        endExclusive,
                    )
                },
            )
        },
    ).flow
        .cachedIn(viewModelScope)
        .flowOn(dispatchers.io)

    // Workaround for leaky QueryDataSource, store the reference so we can release the resources.
    private var factory: QueryDataSourceFactory<SelectAll>? = null

    init {
        val queryProvider = { limit: Long, offset: Long ->
            matchQueries.selectAll(accountId, limit, offset)
        }
        factory = QueryDataSourceFactory(
            queryProvider = queryProvider,
            countQuery = matchQueries.countMatches(accountId),
            transacter = matchQueries,
        )
        response = repository.getMatchesLiveData(
            scope = viewModelScope,
            factory = factory!!.map(SelectAll::mapToUi),
            id = accountId,
        )
        fetchMatches()
    }

    fun fetchMatches() {
        viewModelScope.launch {
            try {
                _refreshStatus.value = Status.LOADING
                repository.fetchMatches(accountId)
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
