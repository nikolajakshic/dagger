package com.nikola.jakshic.dagger.bookmark.player

import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.sqldelight.PlayerBookmarkQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerBookmarkViewModel @Inject constructor(
    playerBookmarkQueries: PlayerBookmarkQueries
) : ScopedViewModel() {

    private val _list = MutableStateFlow<List<PlayerBookmarkUI>>(emptyList())
    val list: StateFlow<List<PlayerBookmarkUI>> = _list

    init {
        launch {
            playerBookmarkQueries.selectAllPlayerBookmark()
                .asFlow()
                .mapToList(Dispatchers.IO)
                .map { it.mapToUi() }
                .flowOn(Dispatchers.IO)
                .collectLatest { _list.value = it }
        }
    }
}