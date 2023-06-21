package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.sqldelight.HeroAssetQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MatchesByHeroViewModel @Inject constructor(
    heroAssetQueries: HeroAssetQueries,
    network: OpenDotaService,
    dispatchers: Dispatchers,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val args = MatchesByHeroFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val pagingData = Pager(
        config = PagingConfig(
            pageSize = 40,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 40,
        ),
        pagingSourceFactory = {
            MatchesByHeroPagingSource(
                accountId = args.accountId,
                heroId = args.heroId,
                network = network,
                dispatchers = dispatchers,
            )
        },
    ).flow
        .cachedIn(viewModelScope)
        .flowOn(dispatchers.io)

    private val heroImage = heroAssetQueries.selectImagePath(args.heroId)
        .asFlow()
        .mapToOneOrNull(dispatchers.io)
        .map { it?.image_path }
        .retryWhen { _, attempt ->
            delay(100)
            return@retryWhen attempt < 3
        }
        .flowOn(dispatchers.io)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    val data = combine(pagingData, heroImage) { pagingData, heroImage ->
        pagingData.map { it.copy(heroImage = heroImage) }
    }.flowOn(dispatchers.io)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty(),
        )
}
