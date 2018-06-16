package com.nikola.jakshic.dagger.ui.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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

    val status = MutableLiveData<Status>()
    lateinit var profile: LiveData<Player>
        private set
    lateinit var bookmark: LiveData<Player>
        private set
    private var initialFetch = false
    private val jobs = Job()
    private val onSuccess: () -> Unit = { status.value = Status.SUCCESS }
    private val onError: () -> Unit = { status.value = Status.ERROR }

    fun getProfile(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            profile = playerDao.getPlayer(id)
            bookmark = bookmarkDao.getPlayer(id)
            fetchProfile(id)
        }
    }

    fun fetchProfile(id: Long) {
        status.value = Status.LOADING
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