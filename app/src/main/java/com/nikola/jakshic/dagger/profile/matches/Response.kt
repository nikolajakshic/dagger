package com.nikola.jakshic.dagger.profile.matches

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.Status

data class Response(
    val pagedList: LiveData<PagedList<MatchUI>>,
    val status: LiveData<Status>,
    val retry: () -> Unit
)
