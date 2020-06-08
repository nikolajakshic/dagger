package com.nikola.jakshic.dagger.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmark
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkDao
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val playerDao: PlayerDao,
    private val playerBookmarkDao: PlayerBookmarkDao,
    private val repo: PlayerRepository
) : ScopedViewModel() {

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
            bookmark = playerBookmarkDao.getPlayer(id)
            fetchProfile(id)
        }
    }

    fun fetchProfile(id: Long) {
        launch {
            try {
                _status.value = Status.LOADING
                repo.getProfile(id)
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun addToBookmark(id: Long) {
        launch {
            withContext(Dispatchers.IO) { playerBookmarkDao.addToBookmark(PlayerBookmark(id)) }
        }
    }

    fun removeFromBookmark(id: Long) {
        launch {
            withContext(Dispatchers.IO) { playerBookmarkDao.removeFromBookmark(id) }
        }
    }
}