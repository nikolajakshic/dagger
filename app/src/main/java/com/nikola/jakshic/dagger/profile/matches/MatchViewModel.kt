package com.nikola.jakshic.dagger.profile.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.paging.DaggerKeyedQueryPagingSource
import com.nikola.jakshic.dagger.common.sqldelight.KeyedQuery
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PAGE_SIZE_LOCAL = 20
private const val PAGE_SIZE_REMOTE = 80L

@HiltViewModel
@OptIn(ExperimentalPagingApi::class)
class MatchViewModel @Inject constructor(
    service: OpenDotaService,
    private val matchQueries: MatchQueries,
    private val matchQueriesExt: MatchQueriesExt,
    private val dispatchers: Dispatchers,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val accountId = MatchFragment.getAccountId(savedStateHandle)

    val data = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE_LOCAL,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = PAGE_SIZE_LOCAL,
        ),
        remoteMediator = MatchRemoteMediator(
            accountId = accountId,
            pageSize = PAGE_SIZE_REMOTE,
            service = service,
            matchQueries = matchQueries,
            dispatchers = dispatchers,
        ),
        pagingSourceFactory = {
            DaggerKeyedQueryPagingSource(
                transacter = matchQueries,
                context = dispatchers.io,
                pageBoundariesProvider = {
                    matchQueriesExt.pageBoundaries(
                        PAGE_SIZE_LOCAL.toLong(),
                        accountId,
                    )
                },
                queryProvider = { beginInclusive: Long?, endExclusive: Long? ->
                    matchQueries.keyedQuery(
                        accountId,
                        beginInclusive,
                        endExclusive,
                    )
                },
            )
        },
    ).flow
        .map { it.map(KeyedQuery::mapToUi) }
        .cachedIn(viewModelScope)
        .flowOn(dispatchers.io)
}
