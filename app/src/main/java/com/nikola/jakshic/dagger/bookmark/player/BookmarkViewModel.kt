package com.nikola.jakshic.dagger.bookmark.player

import androidx.lifecycle.ViewModel
import com.nikola.jakshic.dagger.bookmark.player.BookmarkDao
import javax.inject.Inject

class BookmarkViewModel @Inject constructor(dao: BookmarkDao) : ViewModel() {

    val list = dao.getPlayers()
}