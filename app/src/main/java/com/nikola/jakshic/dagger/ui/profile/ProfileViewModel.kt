package com.nikola.jakshic.dagger.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.data.local.BookmarkDao
import com.nikola.jakshic.dagger.data.local.PlayerDao
import com.nikola.jakshic.dagger.repository.PlayerRepository
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Bookmark
import com.nikola.jakshic.dagger.vo.Player
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val playerDao: PlayerDao,
        private val bookmarkDao: BookmarkDao,
        private val repo: PlayerRepository) : ViewModel() {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status
    lateinit var profile: LiveData<Player>
        private set
    lateinit var bookmark: LiveData<Player>
        private set
    private var initialFetch = false
    private val jobs = Job()
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
        _status.value = Status.LOADING
        repo.getProfile(jobs, id, onSuccess, onError)
    }

    fun addToBookmark(id: Long) {
        launch(UI) {
            withContext(IO) { bookmarkDao.addToBookmark(Bookmark(id)) }
        }
    }

    fun removeFromBookmark(id: Long) {
        launch(UI) {
            withContext(IO) { bookmarkDao.removeFromBookmark(id) }
        }
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}