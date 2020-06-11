package com.nikola.jakshic.dagger.bookmark.match

import com.nikola.jakshic.dagger.common.ScopedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchBookmarkViewModel @Inject constructor(
    private val dao: MatchBookmarkDao
) : ScopedViewModel() {

    val list = dao.getMatches()

    fun updateNote(note: String?, matchId: Long) {
        launch {
            withContext(Dispatchers.IO) { dao.updateNote(note, matchId) }
        }
    }
}