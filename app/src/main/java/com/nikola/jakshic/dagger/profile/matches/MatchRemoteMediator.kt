package com.nikola.jakshic.dagger.profile.matches

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.KeyedQuery
import com.nikola.jakshic.dagger.common.sqldelight.MatchQueries
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MatchRemoteMediator(
    private val accountId: Long,
    private val pageSize: Long,
    private val service: OpenDotaService,
    private val matchQueries: MatchQueries,
    private val dispatchers: Dispatchers,
) : RemoteMediator<Long, KeyedQuery>() {
    private var offset = 0L

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Long, KeyedQuery>,
    ): MediatorResult = withContext(dispatchers.io) {
        try {
            offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return@withContext MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    offset + pageSize
                }
            }

            val matches = if (loadType == LoadType.REFRESH) {
                val count = matchQueries.countMatches(accountId).executeAsOne()
                if (count != 0L) {
                    // There are already some matches in the database
                    // we want to refresh all of them
                    service.getMatches(accountId, count, offset).also {
                        offset -= count - pageSize
                    }
                } else {
                    // There are no matches in the database,
                    // we want to fetch only 20 from the network
                    service.getMatches(accountId, pageSize, offset)
                }
            } else {
                service.getMatches(accountId, pageSize, offset)
            }

            if (matches.isNotEmpty()) {
                matchQueries.transaction {
                    if (loadType == LoadType.REFRESH) {
                        matchQueries.deleteAll(accountId)
                    }
                    matches.forEach { matchQueries.insert(it.mapToDb(accountId = accountId)) }
                }
            }
            MediatorResult.Success(endOfPaginationReached = matches.size < pageSize)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
