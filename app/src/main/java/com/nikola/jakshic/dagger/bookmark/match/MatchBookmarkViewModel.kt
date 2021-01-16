package com.nikola.jakshic.dagger.bookmark.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.sqldelight.MatchBookmarkQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchBookmarkViewModel @Inject constructor(
    private val matchBookmarkQueries: MatchBookmarkQueries
) : ScopedViewModel() {

    private val _list = MutableLiveData<List<MatchBookmarkUI>>()
    val list: LiveData<List<MatchBookmarkUI>>
        get() = _list

    init {
        launch {
            matchBookmarkQueries.selectAllMatchBookmark()
                .asFlow()
                .mapToList(Dispatchers.IO)
                .map { it.mapToUi() }
                .flowOn(Dispatchers.IO)
                .collectLatest { _list.value = it }
        }
    }

    fun updateNote(note: String?, matchId: Long) {
        launch {
            withContext(Dispatchers.IO) { matchBookmarkQueries.update(note, matchId) }
        }
    }
}