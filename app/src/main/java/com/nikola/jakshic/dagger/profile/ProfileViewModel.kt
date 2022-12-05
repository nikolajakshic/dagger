package com.nikola.jakshic.dagger.profile

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkUI
import com.nikola.jakshic.dagger.bookmark.player.mapToUi
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.sqldelight.PlayerBookmarkQueries
import com.nikola.jakshic.dagger.common.sqldelight.PlayerQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
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
            try {
                withContext(Dispatchers.IO) { playerBookmarkQueries.insert(id) }
            } catch (e: SQLiteConstraintException) {
                // Trying to add to the bookmark but player is not yet added to the database.
            }
        }
    }

    fun removeFromBookmark(id: Long) {
        launch {
            withContext(Dispatchers.IO) { playerBookmarkQueries.delete(id) }
        }
    }
}
