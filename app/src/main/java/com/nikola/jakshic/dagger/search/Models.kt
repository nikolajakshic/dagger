package com.nikola.jakshic.dagger.search

import com.nikola.jakshic.dagger.common.sqldelight.Search_history

data class SearchHistoryUI(val query: String)

fun SearchHistoryUI.mapToDb(): Search_history {
    return Search_history(
        id = -1, /* TODO irrelevant, never gonna make it in the database, replace with better model */
        query = query
    )
}

fun Search_history.mapToUi(): SearchHistoryUI {
    return SearchHistoryUI(query)
}