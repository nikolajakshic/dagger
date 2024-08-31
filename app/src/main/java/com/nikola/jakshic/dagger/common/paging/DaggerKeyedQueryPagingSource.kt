package com.nikola.jakshic.dagger.common.paging

import androidx.paging.PagingState
import app.cash.sqldelight.Query
import app.cash.sqldelight.SuspendingTransacter
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransacterBase
import app.cash.sqldelight.TransactionCallbacks
import com.nikola.jakshic.dagger.common.sqldelight.KeyedQuery
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class DaggerKeyedQueryPagingSource(
    private val queryProvider: (beginInclusive: Long?, endExclusive: Long?) -> Query<KeyedQuery>,
    private val pageBoundariesProvider: () -> Query<Long>,
    private val transacter: TransacterBase,
    private val context: CoroutineContext,
) : DaggerQueryPagingSource<Long, KeyedQuery>() {
    private var pageBoundaries: List<Long>? = null
    override val jumpingSupported: Boolean get() = false

    override fun getRefreshKey(state: PagingState<Long, KeyedQuery>): Long? {
        // boundaries: 526, 417, 371, 343, 270, 255, 202
        val boundaries = pageBoundaries
            ?: pageBoundariesProvider()
                .executeAsList()
                .also { pageBoundaries = it }

        return state.anchorPosition?.let anchorPosition@{ anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.let closestItem@{ closestItem ->
                var boundary: Long? = null
                boundaries.forEach {
                    // closestItem.match_id: 317
                    // boundaries: 526, 417, 371, 343, 270, 255, 202
                    // boundary/key: 343
                    if (it >= closestItem.match_id) {
                        boundary = it
                    } else {
                        return@forEach
                    }
                }
                return@closestItem boundary
            }
        }
    }

    override suspend fun load(
        params: LoadParams<Long>,
    ): LoadResult<Long, KeyedQuery> = withContext(context) {
        try {
            val getPagingSourceLoadResult: TransactionCallbacks.() -> LoadResult<Long, KeyedQuery> =
                {
                    // boundaries: 526, 417, 371, 343, 270, 255, 202
                    val boundaries = pageBoundaries
                        ?: pageBoundariesProvider()
                            .executeAsList()
                            .also { pageBoundaries = it }

                    val key = params.key ?: boundaries.firstOrNull() // 343
                    val keyIndex = boundaries.indexOf(key)
                    var previousKey = boundaries.getOrNull(keyIndex - 1) // 371
                    var nextKey = boundaries.getOrNull(keyIndex + 1) // 270

                    val results = mutableListOf<KeyedQuery>()

                    if (key != null && params is LoadParams.Refresh) {
                        val beginInclusive = previousKey ?: key // 371
                        val endExclusive = boundaries.getOrNull(keyIndex + 2) // 255
                        results += queryProvider(beginInclusive, endExclusive)
                            .also { currentQuery = it }
                            .executeAsList()
                        previousKey = boundaries.getOrNull(keyIndex - 2) // 417
                        nextKey = endExclusive // 255
                    } else if (key != null) {
                        results += queryProvider(key, nextKey)
                            .also { currentQuery = it }
                            .executeAsList()
                    } else {
                        currentQuery = queryProvider(null, null)
                    }

                    LoadResult.Page(
                        data = results,
                        prevKey = previousKey,
                        nextKey = nextKey,
                    )
                }
            when (transacter) {
                is Transacter -> transacter.transactionWithResult(bodyWithReturn = getPagingSourceLoadResult)
                is SuspendingTransacter -> transacter.transactionWithResult(bodyWithReturn = getPagingSourceLoadResult)
            }
        } catch (e: Exception) {
            if (e is IllegalArgumentException) throw e
            LoadResult.Error(e)
        }
    }
}
