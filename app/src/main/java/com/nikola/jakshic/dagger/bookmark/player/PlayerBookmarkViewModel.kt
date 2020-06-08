package com.nikola.jakshic.dagger.bookmark.player

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class PlayerBookmarkViewModel @Inject constructor(dao: PlayerBookmarkDao) : ViewModel() {

    val list = dao.getPlayers()
}