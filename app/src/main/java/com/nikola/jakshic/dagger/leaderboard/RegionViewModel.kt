package com.nikola.jakshic.dagger.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {
    private val isInitial = AtomicBoolean(true)

    private val _list = MutableStateFlow<List<LeaderboardUI>>(emptyList())
    val list: StateFlow<List<LeaderboardUI>> = _list

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun initialFetch(region: String) {
        if (isInitial.compareAndSet(true, false)) {
            viewModelScope.launch {
                repository.getLeaderboardFlow(region)
                    .collect { _list.value = it }
            }
            fetchLeaderboard(region)
        }
    }

    fun fetchLeaderboard(region: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.fetchLeaderboard(region)
            } catch (ignored: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
}
