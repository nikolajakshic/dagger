package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.sqldelight.HeroAssetQueries
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesByHeroViewModel @Inject constructor(
    heroAssetQueries: HeroAssetQueries,
    repository: MatchRepository,
    dispatchers: Dispatchers,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val args = MatchesByHeroFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val response = repository.fetchMatchesByHero(args.accountId, args.heroId)
    val matches = response.pagedList
    val status = response.status

    val heroImage = heroAssetQueries.selectImagePath(args.heroId)
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

    fun retry() {
        viewModelScope.launch {
            response.retry()
        }
    }

    fun refresh() = response.refresh()
}
