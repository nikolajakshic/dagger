package com.nikola.jakshic.dagger.common.network

import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Test

class NullPrimitiveAdapterTest {
    @Test(expected = JsonDataException::class)
    fun `without NullPrimitiveAdapter we are not getting default values when fields are null`() {
        val moshi = Moshi.Builder().build() // NullPrimitiveAdapter not added
        val adapter = moshi.adapter(DataTypes::class.java)
        val json = """
            {
                "int": null,
                "long": null,
                "float": null,
                "double": null,
                "boolean": null
            }
        """.trimIndent()
        adapter.fromJson(json) // throws JsonDataException: Expected an int but was NULL
    }

    @Test
    fun `with NullPrimitiveAdapter we are getting default values when fields are null`() {
        val moshi = Moshi.Builder()
            .add(NullPrimitiveAdapter()) // NullPrimitiveAdapter added
            .build()
        val adapter = moshi.adapter(DataTypes::class.java)
        val json = """
            {
                "int": null,
                "long": null,
                "float": null,
                "double": null,
                "boolean": null
            }
        """.trimIndent()
        val dataTypes = adapter.fromJson(json)!!
        assertEquals(0, dataTypes.int)
        assertEquals(0, dataTypes.long)
        assertEquals(0f, dataTypes.float)
        assertEquals(0.0, dataTypes.double, 0.0)
        assertEquals(false, dataTypes.boolean)
    }

    @JsonClass(generateAdapter = true)
    data class DataTypes(
        val int: Int,
        val long: Long,
        val float: Float,
        val double: Double,
        val boolean: Boolean,
    )
}
