package com.nikola.jakshic.dagger.ui.profile.matches

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Match

data class Response(
        val pagedList: LiveData<PagedList<Match>>,
        val status: LiveData<Status>,
        val retry: () -> Unit)