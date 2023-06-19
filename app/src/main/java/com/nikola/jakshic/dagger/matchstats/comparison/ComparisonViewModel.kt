package com.nikola.jakshic.dagger.matchstats.comparison

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.sqldelight.HeroAssetQueries
import com.nikola.jakshic.dagger.common.sqldelight.SelectHeroImagesById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ComparisonViewModel @Inject constructor(
    heroAssetQueries: HeroAssetQueries,
    dispatchers: Dispatchers,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val args = ComparisonDialogArgs.fromSavedStateHandle(savedStateHandle)
    val heroes = heroAssetQueries.selectHeroImagesById(
        args.heroIds[0],
        args.heroIds[1],
        args.heroIds[2],
        args.heroIds[3],
        args.heroIds[4],
        args.heroIds[5],
        args.heroIds[6],
        args.heroIds[7],
        args.heroIds[8],
        args.heroIds[9],
    ).asFlow()
        .mapToOne(dispatchers.io)
        .map(SelectHeroImagesById::mapToUi)
        .retryWhen { _, attempt ->
            delay(100)
            return@retryWhen attempt < 3
        }
        .flowOn(dispatchers.io)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )
}
