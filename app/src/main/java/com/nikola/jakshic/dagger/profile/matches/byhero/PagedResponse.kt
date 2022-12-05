package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.Status

data class PagedResponse<T>(
    val pagedList: LiveData<PagedList<T>>,
    val status: LiveData<Status>,
    val refresh: () -> Unit,
    val retry: suspend () -> Unit
)
