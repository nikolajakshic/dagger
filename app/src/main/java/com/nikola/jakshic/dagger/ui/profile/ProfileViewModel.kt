package com.nikola.jakshic.dagger.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.data.local.BookmarkDao
import com.nikola.jakshic.dagger.data.local.PlayerDao
import com.nikola.jakshic.dagger.repository.PlayerRepository
import com.nikola.jakshic.dagger.ui.ScopedViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Bookmark
import com.nikola.jakshic.dagger.vo.Player
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val playerDao: PlayerDao,
        private val bookmarkDao: BookmarkDao,
        private val repo: PlayerRepository) : ScopedViewModel() {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    lateinit var profile: LiveData<Player>
        private set

    lateinit var bookmark: LiveData<Player>
        private set

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun getProfile(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            profile = playerDao.getPlayer(id)
            bookmark = bookmarkDao.getPlayer(id)
            fetchProfile(id)
        }
    }

    fun fetchProfile(id: Long) {
        launch {
            _status.value = Status.LOADING
            repo.getProfile(id, onSuccess, onError)
        }
    }

    fun addToBookmark(id: Long) {
        launch {
            withContext(Dispatchers.IO) { bookmarkDao.addToBookmark(Bookmark(id)) }
        }
    }

    fun removeFromBookmark(id: Long) {
        launch {
            withContext(Dispatchers.IO) { bookmarkDao.removeFromBookmark(id) }
        }
    }
}