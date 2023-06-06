package com.nikola.jakshic.dagger.common.extensions

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Invocation
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T : Any> Call<T>.awaitNonCancellably(): T {
    return suspendCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null) {
                        val invocation = call.request().tag(Invocation::class.java)!!
                        val method = invocation.method()
                        val e = KotlinNullPointerException(
                            "Response from " +
                                method.declaringClass.name +
                                '.' +
                                method.name +
                                " was null but response body type was declared as non-null",
                        )
                        continuation.resumeWithException(e)
                    } else {
                        continuation.resume(body)
                    }
                } else {
                    continuation.resumeWithException(HttpException(response))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}
