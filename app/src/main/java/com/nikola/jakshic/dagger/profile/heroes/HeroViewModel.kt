package com.nikola.jakshic.dagger.profile.heroes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val KEY_SORT_BY = "sort-by"

@HiltViewModel
class HeroViewModel @Inject constructor(
    private val repository: HeroRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val accountId = HeroFragment.getAccountId(savedStateHandle)

    private val _sortBy = savedStateHandle.getStateFlow(KEY_SORT_BY, SortBy.GAMES.name)
    val sortBy get() = SortBy.valueOf(_sortBy.value)

    val list: StateFlow<List<HeroUI>> = _sortBy.flatMapLatest {
        when (SortBy.valueOf(it)) {
            SortBy.GAMES -> repository.getHeroesByGames(accountId)
            SortBy.WINRATE -> repository.getHeroesByWinrate(accountId)
            SortBy.WINS -> repository.getHeroesByWins(accountId)
            SortBy.LOSSES -> repository.getHeroesByLosses(accountId)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchHeroes()
    }

    fun fetchHeroes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.fetchHeroes(accountId)
            } catch (ignored: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sortBy(sortBy: SortBy) {
        savedStateHandle[KEY_SORT_BY] = sortBy.name
    }
}
