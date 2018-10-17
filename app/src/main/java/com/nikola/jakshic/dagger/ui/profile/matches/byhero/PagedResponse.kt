package com.nikola.jakshic.dagger.ui.profile.matches.byhero

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.ui.Status

data class PagedResponse<T>(
        val pagedList: LiveData<PagedList<T>>,
        val status: LiveData<Status>,
        val refresh: () -> Unit,
        val retry: suspend () -> Unit)