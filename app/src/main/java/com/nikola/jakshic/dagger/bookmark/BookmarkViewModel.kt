package com.nikola.jakshic.dagger.bookmark

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class BookmarkViewModel @Inject constructor(dao: BookmarkDao) : ViewModel() {

    val list = dao.getPlayers()
}