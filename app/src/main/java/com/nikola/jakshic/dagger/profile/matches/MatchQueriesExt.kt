package com.nikola.jakshic.dagger.profile.matches

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchQueriesExt @Inject constructor(
    driver: SqlDriver,
) : TransacterImpl(driver) {
    fun pageBoundaries(limit: Long, anchor: Long?, accountId: Long): Query<Long> =
        PageBoundariesQuery(
            limit,
            anchor,
            accountId,
        ) { cursor ->
            cursor.getLong(0)!!
        }

    private inner class PageBoundariesQuery<out T : Any>(
        val limit: Long,
        val anchor: Long?,
        val accountId: Long,
        mapper: (SqlCursor) -> T,
    ) : Query<T>(mapper) {
        override fun addListener(listener: Listener) {
            driver.addListener("matches", listener = listener)
        }

        override fun removeListener(listener: Listener) {
            driver.removeListener("matches", listener = listener)
        }

        override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
            driver.executeQuery(
                identifier = null,
                sql = """
                    |SELECT match_id
                    |FROM (SELECT matches.match_id,
                    |             CASE
                    |                 WHEN ((row_number() OVER (ORDER BY matches.match_id DESC) - 1) % :limit) = 0 THEN 1
                    |                 WHEN matches.match_id = :anchor THEN 1
                    |                 ELSE 0
                    |                 END page_boundary
                    |      FROM matches
                    |      WHERE matches.account_id = :accountId
                    |      ORDER BY matches.match_id DESC)
                    |WHERE page_boundary = 1
                """.trimMargin(),
                mapper = mapper,
                parameters = 3,
            ) {
                bindLong(0, limit)
                bindLong(1, anchor)
                bindLong(2, accountId)
            }

        override fun toString(): String = "Match.sq:pageBoundaries"
    }
}
