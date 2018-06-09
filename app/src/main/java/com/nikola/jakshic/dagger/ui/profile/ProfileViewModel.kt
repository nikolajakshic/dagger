package com.nikola.jakshic.dagger.ui.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.data.local.BookmarkDao
import com.nikola.jakshic.dagger.data.local.PlayerDao
import com.nikola.jakshic.dagger.vo.Bookmark
import com.nikola.jakshic.dagger.vo.Player
import com.nikola.jakshic.dagger.repository.PlayerRepository
import com.nikola.jakshic.dagger.ui.Status
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
    private val disposables = CompositeDisposable()
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
        disposables.add(repo.getProfile(id, onSuccess, onError))
    }

    fun addToBookmark(id: Long) {
        disposables.add(Completable
                .fromAction { bookmarkDao.addToBookmark(Bookmark(id)) }
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    fun removeFromBookmark(id: Long) {
        disposables.add(Completable
                .fromAction { bookmarkDao.removeFromBookmark(id) }
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    override fun onCleared() {
        disposables.clear()
    }
}