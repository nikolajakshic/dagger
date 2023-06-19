package com.nikola.jakshic.dagger.bookmark.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.sqldelight.MatchBookmarkQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchBookmarkViewModel @Inject constructor(
    private val matchBookmarkQueries: MatchBookmarkQueries,
    private val dispatchers: Dispatchers,
) : ViewModel() {
    val list = matchBookmarkQueries.selectAllMatchBookmark()
        .asFlow()
        .mapToList(dispatchers.io)
        .map { it.mapToUi() }
        .flowOn(dispatchers.io)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun updateNote(matchId: Long, note: String?) {
        viewModelScope.launch {
            withContext(dispatchers.io) { matchBookmarkQueries.update(note, matchId) }
        }
    }
}
