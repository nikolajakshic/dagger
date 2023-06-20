package com.nikola.jakshic.dagger.common.paging

import androidx.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlin.coroutines.CoroutineContext

fun <RowType : Any> QueryPagingSourceFactory(
    countQuery: Query<Long>,
    transacter: Transacter,
    context: CoroutineContext,
    queryProvider: (limit: Long, offset: Long) -> Query<RowType>,
): () -> PagingSource<Int, RowType> {
    return { QueryPagingSource(countQuery, transacter, context, queryProvider) }
}

fun <Key : Any, RowType : Any> QueryPagingSourceFactory(
    transacter: Transacter,
    context: CoroutineContext,
    pageBoundariesProvider: (anchor: Key?, limit: Long) -> Query<Key>,
    queryProvider: (beginInclusive: Key, endExclusive: Key?) -> Query<RowType>,
): () -> PagingSource<Key, RowType> {
    return { QueryPagingSource(transacter, context, pageBoundariesProvider, queryProvider) }
}
