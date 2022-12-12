package com.nikola.jakshic.dagger.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.sqldelight.PlayerBookmarkQueries
import com.nikola.jakshic.dagger.common.sqldelight.PlayerQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    playerQueries: PlayerQueries,
    private val playerBookmarkQueries: PlayerBookmarkQueries,
    private val repo: PlayerRepository,
    private val dispatchers: Dispatchers,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val accountId = ProfileFragmentArgs.fromSavedStateHandle(savedStateHandle).accountId

    val player: StateFlow<PlayerUI?> = playerQueries.select(accountId)
        .asFlow()
        .mapToOneOrNull(dispatchers.io)
        .filterNotNull()
        .map { it.mapToUi() }
        .retryWhen { _, attempt ->
            delay(100)
            return@retryWhen attempt < 3
        }
        .catch { Timber.e(it) }
        .flowOn(dispatchers.io)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val isBookmarked: StateFlow<Boolean> = playerBookmarkQueries.select(accountId)
        .asFlow()
        .mapToOneOrNull(dispatchers.io)
        .map { it != null }
        .retryWhen { _, attempt ->
            delay(100)
            return@retryWhen attempt < 3
        }
        .catch { Timber.e(it) }
        .flowOn(dispatchers.io)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repo.fetchProfile(accountId)
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToBookmark() {
        viewModelScope.launch {
            try {
                withContext(dispatchers.io) { playerBookmarkQueries.insert(accountId) }
            } catch (e: Exception) { // SQLiteConstraintException - Bookmark before player is added to the database.
                Timber.e(e)
            }
        }
    }

    fun removeFromBookmark() {
        viewModelScope.launch {
            try {
                withContext(dispatchers.io) { playerBookmarkQueries.delete(accountId) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
