package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.profile.matches.MatchJson
import com.nikola.jakshic.dagger.profile.matches.MatchUI
import com.nikola.jakshic.dagger.profile.matches.mapToUi
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MatchesByHeroPagingSource(
    private val accountId: Long,
    private val heroId: Long,
    private val network: OpenDotaService,
    private val dispatchers: Dispatchers,
) : PagingSource<Long, MatchUI>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MatchUI> {
        val currentKey = params.key ?: 0
        return try {
            val response = withContext(dispatchers.io) {
                network.getMatchesByHero(
                    playerId = accountId,
                    heroId = heroId,
                    limit = 40,
                    offset = currentKey,
                ).map(MatchJson::mapToUi)
            }
            val prevKey = if (currentKey > 0) currentKey - 40 else null
            val nextKey = if (response.isNotEmpty()) currentKey + 40 else null
            LoadResult.Page(response, prevKey, nextKey)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, MatchUI>): Long? = null
}
