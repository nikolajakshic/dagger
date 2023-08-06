package com.nikola.jakshic.dagger.common.sqldelight

import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.nikola.jakshic.dagger.Database

object DaggerSchema : SqlSchema<QueryResult.Value<Unit>> by Database.Schema {
    override fun migrate(
        driver: SqlDriver,
        oldVersion: Long,
        newVersion: Long,
        vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> {
        if (oldVersion <= 8 && newVersion > 8) {
            val tables = driver.executeQuery(
                identifier = null,
                sql = "SELECT name FROM sqlite_master WHERE type = 'table';",
                mapper = { sqlCursor ->
                    val tables = mutableListOf<String>()
                    while (sqlCursor.next().value) {
                        tables += sqlCursor.getString(0)!!
                    }
                    return@executeQuery QueryResult.Value(tables)
                },
                parameters = 0,
            ).value
            driver.execute(null, "PRAGMA foreign_keys = OFF;", 0)
            for (table in tables) {
                // https://www.sqlite.org/fileformat.html#internal_schema_objects
                if (table != "android_metadata" && !table.startsWith("sqlite_")) {
                    driver.execute(null, "DROP TABLE IF EXISTS $table;", 0)
                }
            }
            driver.execute(null, "PRAGMA foreign_keys = ON;", 0)
            Database.Schema.create(driver)
            return QueryResult.Unit
        }
        Database.Schema.migrate(driver, oldVersion, newVersion, *callbacks)

        return QueryResult.Unit
    }
}
