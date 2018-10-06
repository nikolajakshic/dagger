package com.nikola.jakshic.dagger.ui.bookmark

import androidx.lifecycle.ViewModel
import com.nikola.jakshic.dagger.data.local.BookmarkDao
import javax.inject.Inject

class BookmarkViewModel @Inject constructor(dao: BookmarkDao) : ViewModel() {

    val list = dao.getPlayers()
}