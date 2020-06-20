package com.nikola.jakshic.dagger.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkUI
import com.nikola.jakshic.dagger.bookmark.player.mapToUi
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.sqldelight.Bookmark
import com.nikola.jakshic.dagger.common.sqldelight.PlayerBookmarkQueries
import com.nikola.jakshic.dagger.common.sqldelight.PlayerQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val playerQueries: PlayerQueries,
    private val playerBookmarkQueries: PlayerBookmarkQueries,
    private val repo: PlayerRepository
) : ScopedViewModel() {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _profile = MutableLiveData<PlayerUI>()
    val profile: LiveData<PlayerUI>
        get() = _profile

    private val _bookmark = MutableLiveData<PlayerBookmarkUI>()
    val bookmark: LiveData<PlayerBookmarkUI>
        get() = _bookmark

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun getProfile(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            launch {
                playerQueries.select(id)
                    .asFlow()
                    .mapToOneOrNull(Dispatchers.IO)
                    .map { it?.mapToUi() }
                    .flowOn(Dispatchers.IO)
                    .collectLatest { _profile.value = it }
            }
            launch {
                playerBookmarkQueries.select(id)
                    .asFlow()
                    .mapToOneOrNull(Dispatchers.IO)
                    .map { it?.mapToUi() }
                    .flowOn(Dispatchers.IO)
                    .collectLatest { _bookmark.value = it }
            }
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
            // TODO -1 is irrelevant, never gonna make it in database, create new model that receives only account id and then mapToDb()
            withContext(Dispatchers.IO) { playerBookmarkQueries.insert(Bookmark(-1, id)) }
        }
    }

    fun removeFromBookmark(id: Long) {
        launch {
            withContext(Dispatchers.IO) { playerBookmarkQueries.delete(id) }
        }
    }
}