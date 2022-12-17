package com.nikola.jakshic.dagger.bookmark.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.sqldelight.MatchBookmarkQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchBookmarkViewModel @Inject constructor(
    private val matchBookmarkQueries: MatchBookmarkQueries,
    private val dispatchers: Dispatchers,
) : ViewModel() {
    private val _list = MutableStateFlow<List<MatchBookmarkUI>>(emptyList())
    val list: StateFlow<List<MatchBookmarkUI>> = _list

    init {
        viewModelScope.launch {
            matchBookmarkQueries.selectAllMatchBookmark()
                .asFlow()
                .mapToList(dispatchers.io)
                .map { it.mapToUi() }
                .flowOn(dispatchers.io)
                .collectLatest { _list.value = it }
        }
    }

    fun updateNote(note: String?, matchId: Long) {
        viewModelScope.launch {
            withContext(dispatchers.io) { matchBookmarkQueries.update(note, matchId) }
        }
    }
}
