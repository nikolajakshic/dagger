package com.nikola.jakshic.dagger.bookmark.player

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.sqldelight.PlayerBookmarkQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlayerBookmarkViewModel @ViewModelInject constructor(
    playerBookmarkQueries: PlayerBookmarkQueries
) : ScopedViewModel() {

    private val _list = MutableLiveData<List<PlayerBookmarkUI>>()
    val list: LiveData<List<PlayerBookmarkUI>>
        get() = _list

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