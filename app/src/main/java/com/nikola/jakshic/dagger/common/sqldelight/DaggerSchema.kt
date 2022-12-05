package com.nikola.jakshic.dagger.common.sqldelight

import com.nikola.jakshic.dagger.Database
import com.squareup.sqldelight.db.SqlDriver

object DaggerSchema : SqlDriver.Schema by Database.Schema {
    override fun migrate(driver: SqlDriver, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 8 && newVersion > 8) {
            val cursor = driver.executeQuery(null, "SELECT name FROM sqlite_master WHERE type = 'table';", 0)
            driver.execute(null, "PRAGMA foreign_keys = OFF;", 0)
            while (cursor.next()) {
                val table = cursor.getString(0)!!
                // https://www.sqlite.org/fileformat.html#internal_schema_objects
                if (!table.equals("android_metadata", ignoreCase = true) &&
                    !table.startsWith("sqlite_", ignoreCase = true)
                ) {
                    driver.execute(null, "DROP TABLE IF EXISTS $table;", 0)
                }
            }
            driver.execute(null, "PRAGMA foreign_keys = ON;", 0)
            Database.Schema.create(driver)
        }
        Database.Schema.migrate(driver, oldVersion, newVersion)
    }
}
