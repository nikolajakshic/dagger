package com.nikola.jakshic.dagger.bookmark.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.sqldelight.PlayerBookmarkQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerBookmarkViewModel @Inject constructor(
    playerBookmarkQueries: PlayerBookmarkQueries,
    dispatchers: Dispatchers,
) : ViewModel() {
    val list = playerBookmarkQueries.selectAllPlayerBookmark()
        .asFlow()
        .mapToList(dispatchers.io)
        .map { it.mapToUi() }
        .flowOn(dispatchers.io)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )
}
